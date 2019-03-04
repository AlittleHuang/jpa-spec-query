package com.github.jpa.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.WhereClause;
import com.github.jpa.util.JpaHelper;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Iterator;
import java.util.List;

public class SpecificationImpl<T> implements Specification<T> {
    private final WhereClause<T> whereClause;

    public SpecificationImpl(WhereClause<T> whereClause) {
        this.whereClause = whereClause;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return new PredicateBuilder(root, query, criteriaBuilder, whereClause).toPredicate();
    }

    private class PredicateBuilder {
        private final Root<T> root;
        private final CriteriaQuery<?> query;
        private final CriteriaBuilder cb;
        private final WhereClause<T> item;
        private Predicate predicate;

        private PredicateBuilder(Root<T> root, CriteriaQuery query, CriteriaBuilder criteriaBuilder,
                                 WhereClause<T> item) {
            this.root = root;
            this.query = query;
            this.cb = criteriaBuilder;
            this.item = item;
        }

        private Predicate toPredicate() {
            if (!item.isCompound()) {
                build();
            } else {
                recursiveBuild();
            }
            return predicate == null
                    ? null
                    : (item.isNegate() ? predicate.not() : predicate);
        }

        private void recursiveBuild() {
            List<? extends WhereClause<T>> subItems = item.getCompoundItems();
            if (subItems != null) {
                for (WhereClause<T> item : subItems) {
                    Predicate predicateItem = new PredicateBuilder(root, query, cb, item).toPredicate();
                    if (this.predicate == null) {
                        this.predicate = predicateItem;
                    } else {
                        switch (item.getBooleanOperator()) {
                            case AND:
                                this.predicate = cb.and(this.predicate, predicateItem);
                                break;
                            case OR:
                                this.predicate = cb.or(this.predicate, predicateItem);
                                break;
                        }
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void build() {
            Attribute attribute = item.getPath();
            Path path = toPath(attribute);
            Object value = item.getValue();
            if (value instanceof Attribute) {
                toPredicateItem(path, toPath((Attribute) value));
            } else {
                toPredicateItem(path, value);
            }
        }

        @SuppressWarnings("unchecked")
        private void toPredicateItem(Path path, Object value) {
            switch (item.getConditionalOperator()) {
                case EQUAL:
                    predicate = cb.equal(path, value);
                    break;
                case GREATER_THAN:
                    predicate = cb.greaterThan(path, (Comparable) value);
                    break;
                case LESS_THAN:
                    predicate = cb.lessThan(path, (Comparable) value);
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                    break;
                case BETWEEN:
                    Iterator<?> bt = ((Iterable<?>) value).iterator();
                    Object x = bt.next();
                    Object y = bt.next();
                    if (x instanceof Expression && y instanceof Expression) {
                        predicate = cb.between(path, (Expression) x, (Expression) y);
                    } else {
                        predicate = cb.between(path, (Comparable) x, (Comparable) y);
                    }
                    break;
                case IN:
                    Iterable<?> valuesIn = (Iterable<Comparable>) value;
                    Iterator<?> iterator = valuesIn.iterator();

                    if (!iterator.hasNext()) {
                        predicate = cb.equal(path, path).not();//will get empty result
                        break;
                    }
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for (Object valueIn : valuesIn) {
                        in.value(valueIn);
                    }
                    predicate = in;
                    break;
                case LIKE:
                    predicate = cb.like(path, (String) value);
                    break;
                case IS_NULL:
                    predicate = cb.isNull(path);
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        @SuppressWarnings("unchecked")
        private void toPredicateItem(Path path, Expression value) {
            switch (item.getConditionalOperator()) {
                case EQUAL:
                    predicate = cb.equal(path, value);
                    break;
                case GREATER_THAN:
                    predicate = cb.greaterThan(path, value);
                    break;
                case LESS_THAN:
                    predicate = cb.lessThan(path, value);
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    predicate = cb.greaterThanOrEqualTo(path, value);
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    predicate = cb.lessThanOrEqualTo(path, value);
                    break;
                default:
                    throw new RuntimeException();
            }
        }

        Path toPath(Attribute<T> attribute) {
            return JpaHelper.getPath(root, attribute.getNames(root.getJavaType()));
        }
    }

}

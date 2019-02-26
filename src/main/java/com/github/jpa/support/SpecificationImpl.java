package com.github.jpa.support;

import com.github.data.query.specification.FieldPath;
import com.github.data.query.specification.WhereClause;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class SpecificationImpl<T> implements Specification<T> {
    private final WhereClause whereClause;

    public SpecificationImpl(WhereClause whereClause) {
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
        private final WhereClause item;
        private Predicate predicate;

        private PredicateBuilder(Root<T> root, CriteriaQuery query, CriteriaBuilder criteriaBuilder, WhereClause item) {
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
            List<? extends WhereClause> subItems = item.getCompoundItems();
            if (subItems != null) {
                for (WhereClause item : subItems) {
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
            Path path = item.getPath().getPaths(root);
            Object value = item.getValue();
            if (value instanceof FieldPath) {
                value = ((FieldPath) value).getPaths(root);
            }
            switch (item.getConditionalOperator()) {
                case EQUAL:
                    predicate = cb.equal(path, value);
                    break;
                case GT:
                    predicate = cb.greaterThan(path, (Comparable) value);
                    break;
                case LT:
                    predicate = cb.lessThan(path, (Comparable) value);
                    break;
                case GE:
                    predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                    break;
                case LE:
                    predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                    break;
                case BETWEEN:
                    List<Comparable> valuesBtween = (List<Comparable>) value;
                    predicate = cb.between(path, valuesBtween.get(0), valuesBtween.get(1));
                    break;
                case IN:
                    List<?> valuesIn = (List<Comparable>) value;

                    if (valuesIn.isEmpty()) {
                        predicate = cb.equal(path, path).not();//will get empty result
                        break;
                    }
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for (Object x : valuesIn) {
                        in.value(x);
                    }
                    predicate = in;
                    break;
                case LIKE:
                    //noinspection ConstantConditions
                    predicate = cb.like(path, (String) value);
                    break;
                case IS_NULL:
                    predicate = cb.isNull(path);
                    break;
                default:
            }
        }
    }

}

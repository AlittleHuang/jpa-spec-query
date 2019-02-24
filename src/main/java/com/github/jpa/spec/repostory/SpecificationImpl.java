package com.github.jpa.spec.repostory;

import com.github.jpa.spec.query.api.FieldPath;
import com.github.jpa.spec.query.api.WhereClause;
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

        @SuppressWarnings("unchecked")
        private Predicate toPredicate() {
            if (item.getPath() != null) {
                Path path = item.getPath().getPaths(root);
                Object value = item.getValue();
                if (value instanceof FieldPath) {
                    value = ((FieldPath) value).getPaths(root);
                }
                switch (item.getConditionalOperator()) {
                    //   EQUAL,
                    case EQUAL:
                        predicate = cb.equal(path, value);
                        break;
                    //   GT,
                    case GT:
                        predicate = cb.greaterThan(path, (Comparable) value);
                        break;
                    //   LT,
                    case LT:
                        predicate = cb.lessThan(path, (Comparable) value);
                        break;
                    //   GE,
                    case GE:
                        predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                        break;
                    //   LE,
                    case LE:
                        predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                        break;
                    //   BETWEEN,
                    case BETWEEN:
                        List<Comparable> valuesBtween = (List<Comparable>) value;
                        predicate = cb.between(path, valuesBtween.get(0), valuesBtween.get(1));
                        break;
                    //   IN,
                    case IN:
                        List<?> valuesIn = (List<Comparable>) value;

                        if (valuesIn.isEmpty()) {
                            predicate = cb.equal(path, path).not();
                            break;
                        }
                        CriteriaBuilder.In<Object> in = cb.in(path);
                        for (Object x : valuesIn) {
                            in.value(x);
                        }
                        predicate = in;
                        break;
                    //   LIKE,
                    case LIKE:
                        //noinspection ConstantConditions
                        predicate = cb.like(path, (String) value);
                        break;
                    //   IS_NULL,
                    case IS_NULL:
                        predicate = cb.isNull(path);
                        break;
                    default:
                }

            }
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
            return predicate == null
                    ? null
                    : (item.isNegate() ? predicate.not() : predicate);
        }
    }

}

package com.github.jpa.spec.repostory;

import com.github.jpa.spec.query.api.FieldPath;
import com.github.jpa.spec.query.api.WhereClause;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class SpecificationImpl<T> implements Specification<T> {
    private final WhereClause<?> whereClause;

    public SpecificationImpl(WhereClause<?> whereClause) {
        this.whereClause = whereClause;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return new Item(root, query, criteriaBuilder, whereClause).toPredicate();
    }

    private class Item {
        private final Root<T> root;
        private final CriteriaQuery<?> query;
        private final CriteriaBuilder cb;
        private final WhereClause whereClause;
        private Predicate predicate;

        public Item(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder, WhereClause whereClause) {
            this.root = root;
            this.query = query;
            this.cb = criteriaBuilder;
            this.whereClause = whereClause;
        }

        @SuppressWarnings("unchecked")
        private Predicate toPredicate() {
            if (whereClause.getPath() != null) {
                Path path = whereClause.getPath().getPaths(root);
                Object value = whereClause.getValue();
                if (value instanceof FieldPath) {
                    value = ((FieldPath) value).getPaths(root);
                }
                switch (whereClause.getConditionalOperator()) {
                    //    EQUAL,
                    case EQUAL:
                        predicate = cb.equal(path, value);
                        break;
                    //    GT,
                    case GT:
                        predicate = cb.greaterThan(path, (Comparable) value);
                        break;
                    //    LT,
                    case LT:
                        predicate = cb.lessThan(path, (Comparable) value);
                        break;
                    //    GE,
                    case GE:
                        predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                        break;
                    //    LE,
                    case LE:
                        predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                        break;
                    //    BETWEEN,
                    case BETWEEN:
                        List<Comparable> values = (List<Comparable>) value;
                        predicate = cb.between(path, values.get(0), values.get(1));
                        break;
                    case IN:
//                        List<?> values = (List<Comparable>) value;
//                        if (values.isEmpty()) {
//                            return andNotEqualAsPath(name, name);
//                            break;
//                        }
//                        if (value.size() == 1) {
//                            return andEqual(name, value.iterator().next());
//                            break;
//                        }
//                        CriteriaBuilder.In<Object> in = conditions.cb.in(getPath(name));
//                        for (X x : value) {
//                            in.value(x);
//                        }
                        break;
                    //    IN,
                    //    LIKE,
                    //    IS_NULL,
                }
                return whereClause.isNegate() ? predicate.not() : predicate;
            }
//            if ()
            return null;
        }
    }

}

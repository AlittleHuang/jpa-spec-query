package com.github.alittlehuang.data.jpa.support;

import com.github.alittlehuang.data.query.specification.WhereClause;
import com.github.alittlehuang.data.jpa.util.JpaHelper;
//import org.springframework.data.jpa.page.Specification;

import javax.persistence.criteria.*;
import java.util.Iterator;
import java.util.List;

public class SpecificationImpl<T>/* implements Specification<T>*/ {
    private final WhereClause<T> whereClause;

    public SpecificationImpl(WhereClause<T> whereClause) {
        this.whereClause = whereClause;
    }

    //@Override
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
            if ( !item.isCompound() ) {
                build();
            } else {
                recursiveBuild();
            }
            return predicate == null
                    ? null
                    : ( item.isNegate() ? predicate.not() : predicate );
        }

        private void recursiveBuild() {
            List<? extends WhereClause<T>> subItems = item.getCompoundItems();
            if ( subItems != null ) {
                for ( WhereClause<T> item : subItems ) {
                    Predicate predicateItem = new PredicateBuilder(root, query, cb, item).toPredicate();
                    if ( this.predicate == null ) {
                        this.predicate = predicateItem;
                    } else {
                        switch ( item.getBooleanOperator() ) {
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

        private void build() {
            com.github.alittlehuang.data.query.specification.Expression<T> expressions = item.getExpression();

            javax.persistence.criteria.Expression expression = JpaHelper.toExpression(expressions, cb, root);

            Object value = item.getParameter();

            if ( value instanceof com.github.alittlehuang.data.query.specification.Expression ) {
                //noinspection unchecked
                com.github.alittlehuang.data.query.specification.Expression<T> attr = (com.github.alittlehuang.data.query.specification.Expression<T>) value;
                toPredicateItem(expression, JpaHelper.toExpression(attr, cb, root));
            } else {
                toPredicateItem(expression, value);
            }
        }

        @SuppressWarnings( "unchecked" )
        private void toPredicateItem(javax.persistence.criteria.Expression path, Object value) {

            if ( path.getJavaType() == Number.class ) {
                path = path.as(value.getClass());
            }
            switch ( item.getConditionalOperator() ) {
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
                case BETWEEN: {
                    Iterator<?> values = ( (Iterable<?>) value ).iterator();
                    Object x = values.next();
                    Object y = values.next();
                    if ( x instanceof javax.persistence.criteria.Expression && y instanceof javax.persistence.criteria.Expression ) {
                        predicate = cb.between(path, (javax.persistence.criteria.Expression) x, (javax.persistence.criteria.Expression) y);
                    } else {
                        predicate = cb.between(path, (Comparable) x, (Comparable) y);
                    }
                    break;
                }
                case IN: {
                    Iterable<?> values = (Iterable<Comparable>) value;
                    Iterator<?> iterator = values.iterator();

                    if ( !iterator.hasNext() ) {
                        predicate = cb.equal(path, path).not();//will get empty result
                        break;
                    }
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for ( Object valueIn : values ) {
                        in.value(valueIn);
                    }
                    predicate = in;
                    break;
                }
                case LIKE:
                    predicate = cb.like(path, (String) value);
                    break;
                case IS_NULL:
                    predicate = cb.isNull(path);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        @SuppressWarnings( "unchecked" )
        private void toPredicateItem(javax.persistence.criteria.Expression expression, javax.persistence.criteria.Expression other) {
            switch ( item.getConditionalOperator() ) {
                case EQUAL:
                    predicate = cb.equal(expression, other);
                    break;
                case GREATER_THAN:
                    predicate = cb.greaterThan(expression, other);
                    break;
                case LESS_THAN:
                    predicate = cb.lessThan(expression, other);
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    predicate = cb.greaterThanOrEqualTo(expression, other);
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    predicate = cb.lessThanOrEqualTo(expression, other);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }

    }

}

package com.github.alittlehuang.data.jpa.util;

import com.github.alittlehuang.data.query.specification.Expression;
import com.github.alittlehuang.data.query.specification.Attribute;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class JpaHelper {

    public static <T> Path<?> getPath(Root<T> root, String[] attributeNames) {
        Path path = root;
        for ( String attributeName : attributeNames ) {
            path = path.get(attributeName);
        }
        return path;
    }


    public static <T> boolean hasAttributeNames(Root<T> root, String attributeNames) {
        return hasAttributeNames(root, attributeNames.split("\\."));
    }

    public static <T> boolean hasAttributeNames(Root<T> root, String[] attributeNames) {
        try {
            getPath(root, attributeNames);
        } catch ( IllegalArgumentException e ) {
            return false;
        }
        return true;
    }

    public static <T> javax.persistence.criteria.Expression toExpression(Expression<T> expressions, CriteriaBuilder cb, Root<T> root) {
        Expression<T> subexpression = expressions.getSubexpression();
        javax.persistence.criteria.Expression expression = ( subexpression != null )
                        ? toExpression(subexpression, cb, root)
                        : toPath(root, expressions);
        
        Expression.Function type = expressions.getFunction();
        if ( type == null ) type = Expression.Function.NONE;
        Object[] args = expressions.getArgs();

        javax.persistence.criteria.Expression result = expression;

        switch ( type ) {

            case NONE:
                break;
            case ABS:
                //noinspection unchecked
                result = cb.abs(expression);
                break;


            case SUM:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.sum(expression, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.sum(expression, getExpression(cb, root, args));
                break;


            case PROD:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.prod(expression, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.prod(expression, getExpression(cb, root, args));
                break;


            case DIFF:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.diff(expression, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.diff(expression, getExpression(cb, root, args));
                break;


            case QUOT:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.quot(expression, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.quot(expression, getExpression(cb, root, args));
                break;


            case MOD:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.mod(expression, (Integer) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.mod(expression, getExpression(cb, root, args));
                break;


            case SQRT:
                //noinspection unchecked
                result = cb.sqrt(expression);
                break;


            case CONCAT:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.concat(expression, (String) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.concat(expression, getExpression(cb, root, args));
                break;


            case SUBSTRING:
                if ( args.length == 2 ) {
                    //noinspection unchecked
                    result = cb.substring(expression, (Integer) args[0], (Integer) args[1]);
                } else if ( args.length == 1 ) {
                    //noinspection unchecked
                    result = cb.substring(expression, (Integer) args[0]);
                }
                break;


            case TRIM:
                if ( args == null || args.length == 0 ) {
                    //noinspection unchecked
                    result = cb.trim(expression);
                } else if ( args.length == 1 ) {
                    //noinspection unchecked
                    result = cb.trim((CriteriaBuilder.Trimspec) args[0], expression);
                } else if ( args.length == 2 ) {
                    //noinspection unchecked
                    result = cb.trim((CriteriaBuilder.Trimspec) args[0], (char) args[1], expression);
                }
                break;


            case LOWER:
                //noinspection unchecked
                result = cb.lower(expression);
                break;


            case UPPER:
                //noinspection unchecked
                result = cb.upper(expression);
                break;


            case LENGTH:
                //noinspection unchecked
                result = cb.length(expression);
                break;


            case LOCATE:
                if ( args[0] instanceof Attribute ) {
                    //noinspection unchecked
                    result = cb.locate(expression, getExpression(cb, root, args));
                } else {
                    //noinspection unchecked
                    result = cb.locate(expression, (String) args[0]);
                }
                break;


            case COALESCE:
                if ( args[0] instanceof Attribute ) {
                    result = cb.coalesce(expression, getExpression(cb, root, args));
                } else {
                    result = cb.coalesce(expression, args[0]);
                }
                break;


            case NULLIF:
                if ( isNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.nullif(expression, args[0]);
                } else {
                    result = cb.nullif((javax.persistence.criteria.Expression<?>) expression, getExpression(cb, root, args));
                }
                break;
        }
        return result;
    }

    private static <T> javax.persistence.criteria.Expression getExpression(CriteriaBuilder cb, Root<T> root, Object[] args) {
        //noinspection unchecked
        return toExpression((Expression) args[0], cb, root);
    }

    private static boolean isNotAttrExpression(Object[] args) {
        return args == null || args.length <= 0 || args[0] == null || !( args[0] instanceof Expression );
    }

    private static <T> Path<?> toPath(Root<T> root, Attribute<T> attribute) {
        return getPath(root, attribute.getNames());
    }

}

package com.github.alittlehuang.data.jpa.util;

import com.github.alittlehuang.data.query.specification.AttributePath;
import com.github.alittlehuang.data.query.specification.Expression;

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

    public static <T> javax.persistence.criteria.Expression toExpression(Expression<T> expression, CriteriaBuilder cb, Root<T> root) {
        Expression<T> subexpression = expression.getSubexpression();
        javax.persistence.criteria.Expression exp = ( subexpression != null )
                        ? toExpression(subexpression, cb, root)
                : toPath(root, expression);

        Expression.Function type = expression.getFunction();
        if ( type == null ) type = Expression.Function.NONE;

        Object[] args = expression.getArgs();
        args = args == null ? Expression.EMPTY_ARGS : args;

        javax.persistence.criteria.Expression result = exp;

        switch ( type ) {

            case NONE:
                break;
            case ABS:
                //noinspection unchecked
                result = cb.abs(exp);
                break;


            case SUM:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.sum(exp, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.sum(exp, getExpression(cb, root, args));
                break;


            case PROD:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.prod(exp, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.prod(exp, getExpression(cb, root, args));
                break;


            case DIFF:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.diff(exp, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.diff(exp, getExpression(cb, root, args));
                break;


            case QUOT:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.quot(exp, (Number) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.quot(exp, getExpression(cb, root, args));
                break;


            case MOD:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.mod(exp, (Integer) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.mod(exp, getExpression(cb, root, args));
                break;


            case SQRT:
                //noinspection unchecked
                result = cb.sqrt(exp);
                break;


            case CONCAT:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.concat(exp, (String) args[0]);
                } else
                    //noinspection unchecked
                    result = cb.concat(exp, getExpression(cb, root, args));
                break;


            case SUBSTRING:
                if ( args.length == 2 ) {
                    //noinspection unchecked
                    result = cb.substring(exp, (Integer) args[0], (Integer) args[1]);
                } else if ( args.length == 1 ) {
                    //noinspection unchecked
                    result = cb.substring(exp, (Integer) args[0]);
                }
                break;


            case TRIM:
                if ( args == null || args.length == 0 ) {
                    //noinspection unchecked
                    result = cb.trim(exp);
                } else if ( args.length == 1 ) {
                    //noinspection unchecked
                    result = cb.trim((CriteriaBuilder.Trimspec) args[0], exp);
                } else if ( args.length == 2 ) {
                    //noinspection unchecked
                    result = cb.trim((CriteriaBuilder.Trimspec) args[0], (char) args[1], exp);
                }
                break;


            case LOWER:
                //noinspection unchecked
                result = cb.lower(exp);
                break;


            case UPPER:
                //noinspection unchecked
                result = cb.upper(exp);
                break;


            case LENGTH:
                //noinspection unchecked
                result = cb.length(exp);
                break;


            case LOCATE:
                if ( args[0] instanceof AttributePath) {
                    //noinspection unchecked
                    result = cb.locate(exp, getExpression(cb, root, args));
                } else {
                    //noinspection unchecked
                    result = cb.locate(exp, (String) args[0]);
                }
                break;


            case COALESCE:
                if ( args[0] instanceof AttributePath) {
                    result = cb.coalesce(exp, getExpression(cb, root, args));
                } else {
                    result = cb.coalesce(exp, args[0]);
                }
                break;


            case NULLIF:
                if ( isFirstArgNotAttrExpression(args) ) {
                    //noinspection unchecked
                    result = cb.nullif(exp, args[0]);
                } else {
                    result = cb.nullif((javax.persistence.criteria.Expression<?>) exp, getExpression(cb, root, args));
                }
                break;
            case CUSTOMIZE: {
                javax.persistence.criteria.Expression[] expressions = new javax.persistence.criteria.Expression[args.length + 1];
                int index = 0;
                expressions[index++] = exp;
                for ( Object arg : args ) {
                    expressions[index++] = cb.parameter(arg.getClass());
                }
                result = cb.function(expression.getFunctionName(), Object.class, expressions);
            }
        }
        return result;
    }

    private static <T> javax.persistence.criteria.Expression getExpression(CriteriaBuilder cb, Root<T> root, Object[] args) {
        //noinspection unchecked
        return toExpression((Expression) args[0], cb, root);
    }

    private static boolean isFirstArgNotAttrExpression(Object[] args) {
        return args == null || args.length == 0 || args[0] == null || !( args[0] instanceof Expression );
    }

    private static <T> Path<?> toPath(Root<T> root, AttributePath attribute) {
        return getPath(root, attribute.getNames());
    }

}

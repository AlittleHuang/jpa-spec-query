package com.github.jpa.util;

import com.github.data.query.specification.Attribute;
import com.github.data.query.specification.AttrExpression;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
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

    public static <T> Expression toExpression(AttrExpression<T> expressions, CriteriaBuilder cb, Root<T> root) {
        Path path = toPath(root, expressions);
        AttrExpression.Function type = expressions.getFunction();
        if ( type == null ) type = AttrExpression.Function.NONE;
        Object[] args = expressions.getArgs();

        Expression expression = path;
        switch ( type ) {

            case NONE:
                break;
            case ABS:
                //noinspection unchecked
                expression = cb.abs(path);
                break;


            case SUM:
                //noinspection unchecked
                expression = cb.sum(path);
                break;


            case PROD:
                //noinspection unchecked
                expression = cb.prod(path, toPath(root, (Attribute) args[0]));
                break;


            case DIFF:
                //noinspection unchecked
                expression = cb.diff(path, toPath(root, (Attribute) args[0]));
                break;


            case QUOT:
                //noinspection unchecked
                expression = cb.quot(path, toPath(root, (Attribute) args[0]));
                break;


            case MOD:
                //noinspection unchecked
                expression = cb.mod(path, toPath(root, (Attribute) args[0]));
                break;


            case SQRT:
                //noinspection unchecked
                expression = cb.sqrt(path);
                break;


            case CONCAT:
                //noinspection unchecked
                expression = cb.concat(path, toPath(root, (Attribute) args[0]));
                break;


            case SUBSTRING:
                if ( args.length == 2 ) {
                    //noinspection unchecked
                    expression = cb.substring(path, (Integer) args[0], (Integer) args[1]);
                } else if ( args.length == 1 ) {
                    //noinspection unchecked
                    expression = cb.substring(path, (Integer) args[0]);
                }
                break;


            case TRIM:
                //noinspection unchecked
                expression = cb.trim(path);
                break;


            case LOWER:
                //noinspection unchecked
                expression = cb.lower(path);
                break;


            case UPPER:
                //noinspection unchecked
                expression = cb.upper(path);
                break;


            case LENGTH:
                //noinspection unchecked
                expression = cb.length(path);
                break;


            case LOCATE:
                //noinspection unchecked
                expression = cb.locate(path, (String) args[0]);
                break;


            case COALESCE:
                if ( args[0] instanceof Attribute ) {
                    //noinspection unchecked
                    expression = cb.coalesce(path, toPath(root, (Attribute) args[0]));
                } else {
                    expression = cb.coalesce(path, args[0]);
                }
                break;


            case NULLIF:
                if ( args[0] instanceof Attribute ) {
                    //noinspection unchecked
                    expression = cb.nullif((Expression<?>) path, (Expression<?>) toPath(root, (Attribute) args[0]));
                } else {
                    //noinspection unchecked
                    expression = cb.nullif(path, args[0]);
                }
                break;
        }
        return expression;
    }

    private static <T> Path<?> toPath(Root<T> root, Attribute<T> attribute) {
        return getPath(root, attribute.getNames(root.getJavaType()));
    }

}

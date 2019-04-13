package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.jdbc.JdbcQueryStoredConfig;
import com.github.alittlehuang.data.metamodel.Attribute;
import com.github.alittlehuang.data.metamodel.EntityInformation;
import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.*;
import com.github.alittlehuang.data.util.Assert;
import com.github.alittlehuang.data.util.JointKey;

import javax.persistence.LockModeType;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.regex.Pattern;

import static javax.persistence.criteria.CriteriaBuilder.Trimspec;

@SuppressWarnings("WeakerAccess")
public abstract class AbstractSqlBuilder<T> implements SqlBuilderFactory.SqlBuilder<T> {
    private static final JoinType DEFAULT_JOIN_TYPE = JoinType.LEFT;
    private JdbcQueryStoredConfig config;
    private Criteria<T> criteria;
    private EntityInformation<T, ?> rootEntityInfo;
    private List<Object> args = new ArrayList<>();
    private List<SelectedAttribute> selectedAttributes;
    private Map<JointKey, JoinAttr> joinAttrs;
    private Pageable pageable;

    StringBuilder sql;

    public AbstractSqlBuilder(JdbcQueryStoredConfig config, Criteria<T> criteria) {
        this.config = config;
        this.criteria = criteria;
        rootEntityInfo = getEntityInformation(criteria.getJavaType());
    }

    public AbstractSqlBuilder(JdbcQueryStoredConfig config, Criteria<T> criteria, Pageable pageable) {
        this.config = config;
        this.criteria = criteria;
        rootEntityInfo = getEntityInformation(criteria.getJavaType());
        this.pageable = pageable;
    }

    @Override
    public PrecompiledSqlForEntity<T> listEntityResult() {
        sql = new StringBuilder();
        appendSelectFromEntity();
        int index = sql.length();
        appendWhereClause();
        appendOrders();
        insertJoin(index);
        appendLimit();
        appendLockMode();
        return new PrecompiledSqlForEntity<>(sql.toString(), args, selectedAttributes);
    }

    @Override
    public PrecompiledSql listObjectResult() {
        sql = new StringBuilder();
        appendSelections();
        int index = sql.length();
        appendWhereClause();
        appendGroupings();
        appendOrders();
        insertJoin(index);
        appendLimit();
        appendLockMode();
        return new PrecompiledSql(sql.toString(), args);
    }

    @Override
    public PrecompiledSql count() {
        sql = new StringBuilder();
        sql.append("SELECT\n  COUNT(1) AS count_").append(rootEntityInfo.getTableName()).append("_\n");
        appendFrom(rootEntityInfo);
        sql.append("\n  ");
        int index = sql.length();
        appendWhereClause();
        insertJoin(index);
        return new PrecompiledSql(sql.toString(), args);
    }

    @Override
    public PrecompiledSql exists() {
        sql = new StringBuilder();
        sql.append("SELECT\n  ");
        appendRootTableAlias();
        sql.append(".`").append(rootEntityInfo.getIdAttribute().getColumnName())
                .append("` ");
        appendFrom(rootEntityInfo);
        int index = sql.length();
        appendWhereClause();
        insertJoin(index);
        sql.append("\nLIMIT 1");
        return new PrecompiledSql(sql.toString(), args);
    }

    protected void appendLimit() {
        Long offset = criteria.getOffset();
        Long maxResults = criteria.getMaxResults();
        if ( pageable != null ) {
            offset = pageable.getOffset();
            maxResults = pageable.getPageSize();
        }
        if ( offset != null || maxResults != null ) {
            sql.append("\nLIMIT ")
                    .append(offset == null ? 0 : offset)
                    .append(',')
                    .append(maxResults == null ? Long.MAX_VALUE : maxResults);
        }
    }

    protected void appendLockMode() {
        LockModeType lockModeType = criteria.getLockModeType();
        if ( lockModeType != null ) {
            switch ( lockModeType ) {
                case PESSIMISTIC_READ:
                    sql.append(" LOCK IN SHARE MODE");
                    break;
                case PESSIMISTIC_WRITE:
                case PESSIMISTIC_FORCE_INCREMENT:
                    sql.append(" FOR UPDATE");
                    break;
                default:
                    break;
            }
        }
    }

    protected void appendGroupings() {
        List<? extends Expression<T>> groupings = criteria.getGroupings();
        if ( groupings == null || groupings.isEmpty() ) return;
        sql.append("\nGROUP BY\n  ");
        boolean first = true;
        for ( Expression<T> grouping : groupings ) {
            if ( first ) {
                first = false;
            } else {
                sql.append(",\n  ");
            }
            appendExpression(grouping);
        }
    }

    protected void appendOrders() {
        List<? extends Orders<T>> orders = criteria.getOrders();
        if ( orders != null && !orders.isEmpty() ) {
            sql.append("\nORDER BY\n  ");
            boolean first = true;
            for ( Orders<T> order : orders ) {
                if ( first ) {
                    first = false;
                } else {
                    sql.append(",\n  ");
                }
                appendExpression(order);
                sql.append(" ").append(order.getDirection());
            }

        }
    }

    protected void appendWhereClause() {
        WhereClause<T> whereClause = criteria.getWhereClause();
        if ( whereClause != null && ( !whereClause.isCompound() || !whereClause.getCompoundItems().isEmpty() ) ) {
            sql.append("\nWHERE\n  ");
            appendWhereClause(whereClause);
        }
    }

    protected void insertJoin(int index) {
        if ( joinAttrs != null && !joinAttrs.isEmpty() ) {
            StringBuilder join = new StringBuilder();
            for ( JoinAttr value : joinAttrs.values() ) {
                buildJoin(join, value);
            }
            sql.insert(index, join);
        }
    }

    protected void buildJoin(StringBuilder sql, JoinAttr joinAttr) {
        JoinAttr parent = joinAttr.parent;
        if ( parent != null ) {
            buildJoin(sql, parent);
        }
        if ( !joinAttr.appended ) {
            joinAttr.appended = true;
            sql.append(" \n  ").append(joinAttr.joinType).append(" JOIN `")
                    .append(joinAttr.attrInfo.getTableName())
                    .append("` ");
            joinAttr.appendAlias(sql);
            sql.append(" ON ");

            if ( parent != null ) {
                parent.appendAlias(sql);
            } else {
                appendRootTableAlias(sql);
            }

            sql.append(".`").append(joinAttr.attribute.getJoinColumn().name()).append("`=");
            joinAttr.appendAlias(sql);
            String referenced = joinAttr.attribute.getJoinColumn().referencedColumnName();
            if ( referenced.length() == 0 ) {
                referenced = joinAttr.attrInfo.getIdAttribute().getColumnName();
            }
            sql.append(".`").append(referenced).append('`');

        }
    }

    protected void appendFrom(EntityInformation entityInfo) {
        sql.append("\nFROM\n  `")
                .append(entityInfo.getTableName())
                .append("` ");
        appendRootTableAlias();
    }

    protected void appendSelectFromEntity() {
        selectedAttributes = new ArrayList<>();
        sql.append("SELECT\n  ");
        boolean first = true;
        for ( Attribute attribute : rootEntityInfo.getBasicAttributes() ) {
            if ( first ) {
                first = false;
            } else {
                sql.append(",\n  ");
            }
            appendRootTableAlias();
            sql.append('.');
            appendColumnName(attribute);
            selectedAttributes.add(new SelectedAttribute(attribute));
        }
        List<? extends FetchAttribute<T>> fetchList = criteria.getFetchAttributes();
        if ( fetchList != null && !fetchList.isEmpty() ) {
            for ( FetchAttribute<T> fetch : fetchList ) {
                String[] names = fetch.getNames();
                String[] tmp = new String[names.length + 1];
                System.arraycopy(names, 0, tmp, 0, names.length);

                Attribute attr = rootEntityInfo.getAttribute(names[0]);
                EntityInformation<?, ?> attrInfo = getEntityInformation(attr.getFieldType());
                SelectedAttribute select = new SelectedAttribute(attr, null);
                if ( names.length > 1 ) {
                    for (int i = 1; i < names.length; i++ ) {
                        attr = attrInfo.getAttribute(names[i]);
                        attrInfo = getEntityInformation(attr.getFieldType());
                        select = new SelectedAttribute(attr, select);
                    }
                }
                for ( Attribute attribute : attrInfo.getBasicAttributes() ) {
                    sql.append(",\n  ");
                    tmp[names.length] = attribute.getFieldName();
                    appendAttribute(tmp, fetch.getJoinType());
                    selectedAttributes.add(new SelectedAttribute(attribute, select));
                }
            }
        }

        sql.append(" ");
        appendFrom(rootEntityInfo);
    }

    protected void appendSelections() {
        List<? extends Selection<T>> selections = criteria.getSelections();
        Assert.state(selections != null && !selections.isEmpty(), "selections must not be empty");

        sql.append("SELECT\n  ");
        boolean first = true;
        for ( Selection<T> selection : selections ) {
            if ( first ) {
                first = false;
            } else {
                sql.append(",\n  ");
            }
            AggregateFunctions aggregate = selection.getAggregateFunctions();
            if ( aggregate == null || aggregate == AggregateFunctions.NONE ) {
                appendExpression(selection);
            } else {
                sql.append(aggregate).append("(");
                appendExpression(selection);
                sql.append(")");
            }
        }

        sql.append(" ");
        appendFrom(rootEntityInfo);
    }

    protected void appendColumnName(Attribute attribute) {
        sql.append("`").append(attribute.getColumnName()).append("`");
    }

    protected void appendRootTableAlias() {
        appendRootTableAlias(sql);
    }

    protected void appendRootTableAlias(StringBuilder sql) {
        sql.append(rootEntityInfo.getTableName()).append("0_");
    }

    protected void appendWhereClause(WhereClause<T> whereClause) {
        if ( whereClause.isCompound() ) {
            appendCompoundWhereClause(whereClause);
        } else {
            appendNonCompoundWhereClause(whereClause);
        }
    }

    protected void appendNonCompoundWhereClause(WhereClause<T> item) {
        Expression<T> expression = item.getExpression();
        boolean negate = item.isNegate();
        appendExpression(expression);
        switch ( item.getConditionalOperator() ) {
            case EQUAL:
                appendComparisonOperatorExpression(item, negate ? "<>" : "=");
                break;
            case GREATER_THAN:
                appendComparisonOperatorExpression(item, negate ? "<=" : ">");
                break;
            case LESS_THAN:
                appendComparisonOperatorExpression(item, negate ? ">=" : "<");
                break;
            case GREATER_THAN_OR_EQUAL_TO:
                appendComparisonOperatorExpression(item, negate ? "<" : ">=");
                break;
            case LESS_THAN_OR_EQUAL_TO:
                appendComparisonOperatorExpression(item, negate ? ">" : "<=");
                break;
            case BETWEEN: {
                Iterator<?> iterator = ( (Iterable<?>) item.getParameter() ).iterator();
                sql.append(negate ? " NOT BETWEEN " : " BETWEEN ");
                appendSimpleParam(iterator.next());
                sql.append(" AND ");
                appendSimpleParam(iterator.next());
                break;
            }
            case IN: {
                sql.append(negate ? " NOT IN(" : " IN(");
                appendSqlParameter(item.getParameter());
                sql.append(")");
                break;
            }
            case LIKE:
                appendComparisonOperatorExpression(item, negate ? " NOT LIKE " : " LIKE ");
                break;
            case IS_NULL:
                sql.append(negate ? " IS NOT NULL" : " IS NULL");
                break;
        }

    }

    protected void appendComparisonOperatorExpression(WhereClause<T> item, String operator) {
        Object parameter = item.getParameter();
        sql.append(operator);
        appendSqlParameter(parameter);
    }

    protected void appendSqlParameter(Object parameter) {
        if ( parameter instanceof Expression ) {
            //noinspection unchecked
            appendExpression((Expression<T>) parameter);
        } else if ( parameter instanceof Iterable ) {
            boolean first = true;
            for ( Object arg : ( (Iterable<?>) parameter ) ) {
                if ( first ) {
                    first = false;
                } else {
                    sql.append(',');
                }
                appendSimpleParam(arg);
            }
        } else {
            appendSimpleParam(parameter);
        }
    }

    private static Set<Class<?>> basic = new HashSet<>(Arrays.asList(
//            java.lang.Boolean.class,
//            java.lang.Character.class,
            java.lang.Byte.class,
            java.lang.Short.class,
            java.lang.Integer.class,
            java.lang.Long.class,
            java.lang.Float.class,
            java.lang.Double.class
    ));
    
    protected void appendSimpleParam(Object arg) {
        if (arg != null && basic.contains(arg.getClass())) {
            sql.append(arg);
        } else {
            sql.append("?");
            args.add(arg);
        }
    }

    protected void appendFunArg(Object[] parameter) {
        boolean first = true;
        for ( Object o : parameter ) {
            if ( first ) {
                first = false;
            } else {
                sql.append(",");
            }
            appendSqlParameter(o);
        }
    }

    protected void appendExpression(Expression<T> expression) {
        Expression.Function function = expression.getFunction();
        function = function == null ? Expression.Function.NONE : function;
        Object[] args = expression.getArgs();
        switch ( function ) {
            case NONE:
                appendAttribute(expression);
                break;
            case ABS:
                appendSingleParameterFunction(expression, "ABS");
                break;
            case SUM: {
                appendAttribute(expression.getSubexpression());
                sql.append("+");
                Object arg = args[0];
                appendSqlParameter(arg);
                break;
            }
            case PROD: {
                appendAttribute(expression.getSubexpression());
                sql.append("*");
                Object arg = args[0];
                appendFunArgs(arg);
                break;
            }
            case DIFF: {
                appendAttribute(expression.getSubexpression());
                sql.append("-");
                Object arg = args[0];
                appendFunArgs(arg);
                break;
            }
            case QUOT: {
                appendAttribute(expression.getSubexpression());
                sql.append("/");
                Object arg = args[0];
                appendFunArgs(arg);
                break;
            }
            case MOD:
                String mod = "MOD";
                appendMultiParameterFunction(expression, mod);
                break;
            case SQRT:
                appendSingleParameterFunction(expression, "SQRT");
                break;
            case CONCAT:
                String concat = "CONCAT";
                appendMultiParameterFunction(expression, concat);
                break;
            case SUBSTRING:
                appendMultiParameterFunction(expression, "SUBSTRING");
                break;
            case TRIM:
                if ( args == null || args.length == 0 ) {
                    appendSingleParameterFunction(expression, "TRIM");
                } else {
                    Trimspec p0 = (Trimspec) args[0];
                    char p1 = ' ';
                    if ( args.length == 2 ) {
                        p1 = (char) args[1];
                    }
                    sql.append("TRIM(").append(p0).append(" '").append(p1).append("' FROM ");
                    appendExpression(expression.getSubexpression());
                    sql.append(")");
                }
                break;
            case LOWER:
                appendSingleParameterFunction(expression, "LOWER");
                break;
            case UPPER:
                appendSingleParameterFunction(expression, "UPPER");
                break;
            case LENGTH:
                appendSingleParameterFunction(expression, "UPPER");
                break;
            case LOCATE:
                sql.append("LOCATE").append("(");
                appendFunArg(args);
                sql.append(",");
                appendAttribute(expression.getSubexpression());
                sql.append(")");
                break;
            case COALESCE:
                appendMultiParameterFunction(expression, "COALESCE");
                break;
            case NULLIF:
                appendMultiParameterFunction(expression, "NULLIF");
                break;
            case CUSTOMIZE:
                String functionName = expression.getFunctionName();
                if ( expression.getArgs() == null || expression.getArgs().length == 0 ) {
                    appendSingleParameterFunction(expression, functionName);
                } else {
                    appendMultiParameterFunction(expression, functionName);
                }
                break;
        }

    }

    protected void appendMultiParameterFunction(Expression<T> expression, String funStr) {
        sql.append(funStr).append("(");
        appendAttribute(expression.getSubexpression());
        sql.append(",");
        appendFunArg(expression.getArgs());
        sql.append(")");
    }

    private static Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");
    protected void appendSingleParameterFunction(Expression<T> expression, String funStr) {
        Assert.state(pattern.matcher(funStr).matches(), "function name error");
        sql.append(funStr).append("(");
        appendExpression(expression.getSubexpression());
        sql.append(")");
    }

    protected void appendFunArgs(Object arg) {
        if ( arg instanceof Expression ) {
            //noinspection unchecked
            Expression<T> ex = (Expression<T>) arg;
            boolean lowPriority = ex.getFunction() == Expression.Function.SUM
                    || ex.getFunction() == Expression.Function.DIFF;
            if ( lowPriority ) {
                sql.append("(");
            }
            appendExpression(ex);
            if ( lowPriority ) {
                sql.append(")");
            }
        } else {
            appendSqlParameter(arg);
        }
    }

    protected void appendAttribute(AttributePath attribute) {
        String[] names = attribute.getNames();
        appendAttribute(names, null);
    }

    protected void appendAttribute(String[] names, JoinType joinType) {
        Attribute attr = rootEntityInfo.getAttribute(names[0]);
        if ( names.length > 1 ) {
            joinAttrs = joinAttrs == null ? new HashMap<>() : joinAttrs;
            JoinAttr joinAttr = null;
            for (int i = 1; i < names.length; i++ ) {
                JointKey key = new JointKey(joinAttr, attr);
                if ( !joinAttrs.containsKey(key) ) {
                    joinAttrs.put(key, new JoinAttr(joinAttr, attr));
                }
                joinAttr = joinAttrs.get(key);
                if ( i == names.length - 1 ) {
                    joinAttr.joinType = joinType == null ? DEFAULT_JOIN_TYPE : joinType;
                } else if ( joinAttr.joinType == null ) {
                    joinAttr.joinType = DEFAULT_JOIN_TYPE;
                }

                EntityInformation attrInfo = getEntityInformation(attr.getFieldType());
                attr = attrInfo.getAttribute(names[i]);
                if ( !attr.isEntityType() ) {
                    joinAttr.appendAlias(sql);
                    sql.append('.');
                }
            }
        } else {
            appendRootTableAlias();
            sql.append('.');
        }
        appendColumnName(attr);
    }

    protected void appendCompoundWhereClause(WhereClause<T> whereClause) {

        int appendIndex = sql.length();

        List<? extends WhereClause<T>> items = whereClause.getCompoundItems();
        if ( items.size() == 1 ) {
            appendNonCompoundWhereClause(items.get(0));
        } else if ( !items.isEmpty() ) {
            boolean first = true;
            Predicate.BooleanOperator pre = null;
            for ( WhereClause<T> item : items ) {

                if ( first ) {
                    first = item.isCompound();
                } else {
                    Predicate.BooleanOperator operator = item.getBooleanOperator();
                    if ( pre == Predicate.BooleanOperator.OR && operator == Predicate.BooleanOperator.AND ) {
                        sql.insert(appendIndex, "(").append(")");
                    }
                    sql.append(operator == Predicate.BooleanOperator.OR ? "\n  OR  " : "\n  AND ");
                    pre = operator;
                }
                boolean compound = item.isCompound() && item.getCompoundItems().size() > 1;
                if ( compound ) {
                    sql.append("(");
                }
                appendWhereClause(item);

                if ( compound ) {
                    sql.append(")");
                }
            }
        }
    }

    public <X> EntityInformation<X, ?> getEntityInformation(Class<X> clazz) {
        EntityInformation<X, ?> info = config.getEntityInformationFactory().getEntityInfor(clazz);
        Assert.notNull(info, "the type " + clazz + " is not an entity type");
        return info;
    }

    class JoinAttr {
        Attribute attribute;
        EntityInformation attrInfo;
        JoinAttr parent;
        JoinType joinType;
        boolean appended = false;
        int index = joinAttrs.size();

        public JoinAttr(JoinAttr parent, Attribute attribute) {
            this.parent = parent;
            this.attribute = attribute;
            this.attrInfo = getEntityInformation(attribute.getFieldType());
        }

        void appendAlias(StringBuilder sql) {
            sql.append(attrInfo.getTableName())
                    .append(index)
                    .append("_");
        }
    }
}

package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.log.Logger;
import com.github.alittlehuang.data.log.LoggerFactory;

import java.util.List;

/**
 * @author ALittleHuang
 */
public abstract class AbstractPrecompiledSql {
    private static Logger logger = LoggerFactory.getLogger(AbstractPrecompiledSql.class);

    protected String sql;
    protected List<?> args;

    public AbstractPrecompiledSql(String sql, List<?> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql() {
        return sql;
    }

    public List<?> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "QueryPrecompiledSql{\n" +
                "  sql='" + sql + '\'' +
                "\n  args=" + args +
                "\n}";
    }

}

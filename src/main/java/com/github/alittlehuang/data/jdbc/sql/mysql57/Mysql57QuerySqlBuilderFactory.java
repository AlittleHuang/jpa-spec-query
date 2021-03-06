package com.github.alittlehuang.data.jdbc.sql.mysql57;

import com.github.alittlehuang.data.jdbc.JdbcQueryStoredConfig;
import com.github.alittlehuang.data.jdbc.sql.AbstractSqlBuilder;
import com.github.alittlehuang.data.jdbc.sql.QuerySqlBuilderFactory;
import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.Criteria;

/**
 * @author ALittleHuang
 */
public class Mysql57QuerySqlBuilderFactory implements QuerySqlBuilderFactory {

    private JdbcQueryStoredConfig config;

    public Mysql57QuerySqlBuilderFactory(JdbcQueryStoredConfig config) {
        this.config = config;
    }

    @Override
    public <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria) {
        return new Builder<>(criteria);
    }

    @Override
    public <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria, Pageable pageable) {
        return new Builder<>(criteria);
    }

    class Builder<T> extends AbstractSqlBuilder<T> {
        Builder(Criteria<T> criteria) {
            super(config, criteria);
        }
    }
}

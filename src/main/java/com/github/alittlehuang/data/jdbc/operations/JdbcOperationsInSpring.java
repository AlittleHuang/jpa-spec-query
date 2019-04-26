package com.github.alittlehuang.data.jdbc.operations;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Manage transactions by spring, relying on spring-jdbc.
 */
public class JdbcOperationsInSpring extends AbstractJdbcOperations {

    private JdbcTemplate jdbcTemplate;

    public JdbcOperationsInSpring(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcOperationsInSpring() {
    }

    protected <R> R execute(ConnectionCallback<R> action, boolean commit) {
        return jdbcTemplate.execute(action::doInConnection);
    }

}

package com.github.alittlehuang.data.jdbc.operations;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Operating the database in Connection requires manual management of transactions.
 */
public class JdbcOperationsInConnection extends AbstractJdbcOperations {

    @Getter
    @Setter
    private Connection connection;

    public JdbcOperationsInConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    <R> R execute(ConnectionCallback<R> action, boolean commit) {
        try {
            return action.doInConnection(connection);
        } catch ( SQLException ex ) {
            throw new RuntimeException(ex);
        }
    }

}

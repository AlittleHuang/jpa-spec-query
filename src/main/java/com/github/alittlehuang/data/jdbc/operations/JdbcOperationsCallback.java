package com.github.alittlehuang.data.jdbc.operations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface JdbcOperationsCallback {

    /**
     * Create a statement in this connection. Allows implementations to use
     * PreparedStatements. The JdbcOperations will close the created statement.
     *
     * @param con the connection used to create statement
     * @return a prepared statement
     */
    PreparedStatement createPreparedStatement(Connection con) throws SQLException;

    /**
     * Set parameter values on the given PreparedStatement.
     *
     * @param ps the PreparedStatement to invoke setter methods on
     */
    void setValues(PreparedStatement ps) throws SQLException;


}

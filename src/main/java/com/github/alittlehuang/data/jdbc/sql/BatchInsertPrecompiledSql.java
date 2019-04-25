package com.github.alittlehuang.data.jdbc.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author ALittleHuang
 */
public class BatchInsertPrecompiledSql extends BatchUpdatePrecompiledSql {

    public BatchInsertPrecompiledSql(String sql, List<List<?>> args) {
        super(sql, args);
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        return con.prepareStatement(getSql(), Statement.RETURN_GENERATED_KEYS);
    }
}

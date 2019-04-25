package com.github.alittlehuang.data.jdbc.operations;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCallback<T> {

    T doInConnection(Connection con) throws SQLException;

}

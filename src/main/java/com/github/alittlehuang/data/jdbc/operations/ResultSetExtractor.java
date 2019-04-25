package com.github.alittlehuang.data.jdbc.operations;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetExtractor<T> {

    /**
     * Implementations must implement this method to process the entire ResultSet.
     *
     * @param rs the ResultSet to extract data from. Implementations should
     *           not close this: it will be closed by the calling JdbcOperations.
     * @return an arbitrary result object, or {@code null} if none
     * (the extractor will typically be stateful in the latter case).
     */
    T extractData(ResultSet rs) throws SQLException;

}

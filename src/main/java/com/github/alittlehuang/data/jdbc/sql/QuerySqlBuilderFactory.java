package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.Criteria;

/**
 * @author ALittleHuang
 */
public interface QuerySqlBuilderFactory {

    <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria);

    <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria, Pageable pageable);

    interface SqlBuilder<T> {

        PrecompiledSqlForEntity<T> listEntityResult();

        PrecompiledSql listObjectResult();

        PrecompiledSql count();

        PrecompiledSql exists();

    }

}

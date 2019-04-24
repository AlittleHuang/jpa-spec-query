package com.github.alittlehuang.data.jdbc.sql;

import com.github.alittlehuang.data.query.page.Pageable;
import com.github.alittlehuang.data.query.specification.Criteria;

/**
 * @author ALittleHuang
 */
public interface SqlBuilderFactory {

    <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria);

    <T> SqlBuilder<T> createSqlBuild(Criteria<T> criteria, Pageable pageable);

    interface SqlBuilder<T> {

        PrecompiledSqlForEntity<T> listEntityResult();

        QueryPrecompiledSql listObjectResult();

        QueryPrecompiledSql count();

        QueryPrecompiledSql exists();

    }

}

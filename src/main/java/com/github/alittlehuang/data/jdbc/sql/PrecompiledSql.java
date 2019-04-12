package com.github.alittlehuang.data.jdbc.sql;

import java.util.List;

public class PrecompiledSql {

    private String sql;
    private List<Object> args;

    public PrecompiledSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    public String getSql(){
        return sql;
    }

    public List<Object> getArgs(){
        return args;
    }

}

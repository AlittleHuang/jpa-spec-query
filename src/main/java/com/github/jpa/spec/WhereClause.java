package com.github.jpa.spec;


import java.util.List;

public interface WhereClause {

    List<ClauseItem<?>> getCompoundItems();

    List<WhereClause> getSubItems();

}

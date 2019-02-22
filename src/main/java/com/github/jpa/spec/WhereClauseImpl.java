package com.github.jpa.spec;


import lombok.Getter;

import java.util.List;

@Getter
public class WhereClauseImpl implements WhereClause {

    private List<ClauseItem<?>> compoundItems;
    private List<WhereClause> subItems;


}

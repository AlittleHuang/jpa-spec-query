package com.github.jpa.support;

import com.github.data.query.support.AbstractWhereClauseBuilder;
import com.github.data.query.support.SimpleFieldPath;
import com.github.data.query.support.WhereClauseItem;
import lombok.experimental.Delegate;
import org.springframework.data.jpa.domain.Specification;

public class SpecBuilder<T> extends AbstractWhereClauseBuilder<T, SpecBuilder<T>> implements Specification<T> {

    @Override
    protected SpecBuilder<T> createSub(SimpleFieldPath<T> paths) {
        return new SpecBuilder<>(paths, getWhereClause());
    }

    public SpecBuilder() {
    }

    private SpecBuilder(SimpleFieldPath path, WhereClauseItem root) {
        super(path, root);
    }

    @Delegate
    private Specification<T> getSpecification() {
        return new SpecificationImpl<>(getWhereClause());
    }
}

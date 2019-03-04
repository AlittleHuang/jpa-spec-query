package com.github.jpa.support;

import com.github.data.query.specification.Attribute;
import com.github.data.query.support.AbstractWhereClauseBuilder;
import com.github.data.query.support.SimpleAttribute;
import com.github.data.query.support.WhereClauseItem;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor
public class SpecBuilder<T> extends AbstractWhereClauseBuilder<T, SpecBuilder<T>> implements Specification<T> {

    @Override
    protected SpecBuilder<T> createSubItem(Attribute<T> paths) {
        return new SpecBuilder<>(paths, getWhereClause());
    }

    private SpecBuilder(Attribute path, WhereClauseItem<T> root) {
        super(path, root);
    }

    @Delegate
    private Specification<T> getSpecification() {
        return new SpecificationImpl<>(getWhereClause());
    }
}

package com.github.jpa.support;

import com.github.data.query.specification.Expression;
import com.github.data.query.support.AbstractWhereClauseBuilder;
import com.github.data.query.support.SimpleWhereClause;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@NoArgsConstructor
public class SpecBuilder<T> extends AbstractWhereClauseBuilder<T, SpecBuilder<T>> implements Specification<T> {

    @Override
    protected SpecBuilder<T> createSubItem(Expression<T> paths) {
        return new SpecBuilder<>(paths, getWhereClause());
    }

    private SpecBuilder(Expression<T> path, SimpleWhereClause<T> root) {
        super(path, root);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return new SpecificationImpl<>(getWhereClause()).toPredicate(root, criteriaQuery, criteriaBuilder);
    }
}

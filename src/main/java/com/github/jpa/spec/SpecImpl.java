package com.github.jpa.spec;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

public class SpecImpl<T> implements Specification<T> {

    private final Consumer<Criteria<T>> criteriaConsumer;

    public SpecImpl(Consumer<Criteria<T>> criteriaConsumer) {
        this.criteriaConsumer = criteriaConsumer;
    }

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder criteriaBuilder) {
        Criteria<T> criteria =
                new CriteriaImpl<>(root, query, criteriaBuilder);
        criteriaConsumer.accept(criteria);
        return criteria.toPredicate();
    }

    public static <T> Specification<T> build(Consumer<Criteria<T>> consumer) {
        return ((root, query, criteriaBuilder) -> {
            Criteria<T> criteria =
                    new CriteriaImpl<>(root, query, criteriaBuilder);
            consumer.accept(criteria);
            return criteria.toPredicate();
        });
    }

    void test(JpaSpecificationExecutor<T> j) {
        j.findAll(new SpecImpl<>(criteria -> {
        }));
    }
}

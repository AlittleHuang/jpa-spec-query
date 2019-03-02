package com.github.test;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
public class TransactionalService {

    /**
     * 支持当前事务，如果不存在则创建新事务。
     */
    @Transactional
    public <T> T required(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 支持当前事务，如果不存在则创建新事务。
     */
    @Transactional
    public void required(Runnable runnable) {
        runnable.run();
    }

    /**
     * 支持当前事务，如果不存在则以非事务方式执行。
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public <T> T supports(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 支持当前事务，如果不存在则以非事务方式执行。
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    public void supports(Runnable runnable) {
        runnable.run();
    }

    /**
     * 支持当前事务，如果不存在则抛出异常
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public <T> T mandatory(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 支持当前事务，如果不存在则抛出异常
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void mandatory(Runnable runnable) {
        runnable.run();
    }

}

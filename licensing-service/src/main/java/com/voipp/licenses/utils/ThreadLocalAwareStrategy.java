package com.voipp.licenses.utils;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy strategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        if (strategy != null)
            return strategy.getBlockingQueue(maxQueueSize);

        return super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(
            HystrixRequestVariableLifecycle<T> rv) {
        return strategy != null
                ? strategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        return strategy != null
                ? strategy.getThreadPool(threadPoolKey, corePoolSize,
                maximumPoolSize, keepAliveTime, unit, workQueue)
                : super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        DelegatingUserContextCallable<T> userContextCallable =
                new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext());

        if (strategy != null) {
            return strategy.wrapCallable(userContextCallable);
        } else
            return super.wrapCallable(userContextCallable);
    }
}

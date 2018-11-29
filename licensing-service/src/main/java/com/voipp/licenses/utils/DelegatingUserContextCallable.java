package com.voipp.licenses.utils;

import java.util.concurrent.Callable;

public class DelegatingUserContextCallable<V> implements Callable<V> {
    public DelegatingUserContextCallable(Callable<T> callable, UserContext context) {

    }

    @Override
    public V call() throws Exception {
        return null;
    }
}

package com.hobi.util;

import java.util.function.Function;

@FunctionalInterface
public interface ChkExpFreeFunction<T, R> extends Function<T, R> {
    R applyWithException(T t) throws Throwable;

    @Override
    default R apply(T t) {
        try {
            return applyWithException(t);
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

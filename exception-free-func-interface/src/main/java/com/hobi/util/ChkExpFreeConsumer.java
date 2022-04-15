package com.hobi.util;

import java.util.function.Consumer;

@FunctionalInterface
public interface ChkExpFreeConsumer<T> extends Consumer<T> {
    void acceptWithException(T t) throws Throwable;

    @Override
    default void accept(T t) {
        try {
            acceptWithException(t);
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

}

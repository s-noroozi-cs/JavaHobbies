package com.hobi.util;

import java.util.function.Supplier;

@FunctionalInterface
public interface ChkExpFreeSupplier<T> extends Supplier<T> {
    T getWithException() throws Throwable;

    @Override
    default T get() {
        try {
            return getWithException();
        } catch (Throwable ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}

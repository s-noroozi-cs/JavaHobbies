package com.hobi.proxyaspect.logging;

public class MemoryAppenderInstance {
    private static MemoryAppender instance;

    private MemoryAppenderInstance() {
    }

    static void setInstance(MemoryAppender instance) {
        MemoryAppenderInstance.instance = instance;
    }

    public static MemoryAppender getInstance() {
        return instance;
    }

}

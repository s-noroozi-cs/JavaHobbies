package com.example.asyncservlet;

import javax.servlet.AsyncContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AsyncContextHolder {
    private static final Map<String, AsyncContext> contextHolder = new ConcurrentHashMap<>();

    public static void addContext(String key,AsyncContext asyncContext){
        contextHolder.put(key,asyncContext);
    }

    public static AsyncContext getAndRemove(String key){
        AsyncContext asyncContext = contextHolder.get(key);
        contextHolder.remove(key);
        return asyncContext;
    }
}

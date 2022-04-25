package com.hobi.proxyaspect.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class MemoryAppender extends AppenderBase<ILoggingEvent>{
    private final List<ILoggingEvent> events = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        synchronized (events) {
            events.add(event);
        }
    }

    @Override
    public void start() {
        super.start();
        MemoryAppenderInstance.setInstance(this);
    }

    @Override
    public void stop() {
        MemoryAppenderInstance.setInstance(null);
        super.stop();
        events.clear();
    }

    public List<ILoggingEvent> getEvents() {
        List<ILoggingEvent> retVal = new ArrayList<>();
        synchronized (events) {
            retVal.addAll(events);
        }
        return retVal;
    }

    public void clear(){
        synchronized (events){
            events.clear();
        }
    }
}

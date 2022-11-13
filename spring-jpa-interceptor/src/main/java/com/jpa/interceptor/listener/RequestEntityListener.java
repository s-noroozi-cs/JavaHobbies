package com.jpa.interceptor.listener;

import com.jpa.interceptor.entity.Request;
import org.hibernate.Hibernate;

import javax.persistence.PostLoad;

public class RequestEntityListener {

    @PostLoad
    private void afterLoad(Request request) {
        Hibernate.initialize(request);
    }
}

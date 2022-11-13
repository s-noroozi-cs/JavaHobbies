package com.jpa.interceptor.listener;

import com.jpa.interceptor.entity.Request;

import javax.persistence.PostLoad;

public class RequestEntityListener {

    @PostLoad
    private void afterLoad(Request request) {
        // did not help to customize lazy loading based on own biz
        //Hibernate.initialize(request);
    }
}

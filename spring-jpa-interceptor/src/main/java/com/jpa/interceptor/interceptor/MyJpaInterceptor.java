package com.jpa.interceptor.interceptor;

import com.jpa.interceptor.entity.Request;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import java.io.Serializable;

public class MyJpaInterceptor extends EmptyInterceptor {

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if(entity instanceof Request s){
            if(s.getOriginal() == null){
                Hibernate.initialize(s.getOriginal());
            }
        }
        return super.onLoad(entity, id, state, propertyNames, types);
    }
}

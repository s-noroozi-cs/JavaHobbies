package com.jpa.interceptor.repository;

import com.jpa.interceptor.entity.Request;
import com.jpa.interceptor.model.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request,Long> {

    List<Request> findByType(RequestType type);
}

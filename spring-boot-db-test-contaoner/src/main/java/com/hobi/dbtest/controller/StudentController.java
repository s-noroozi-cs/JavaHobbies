package com.hobi.dbtest.controller;

import com.hobi.dbtest.entity.Student;
import com.hobi.dbtest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/students")
public class StudentController {
    private StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity saveNewStudent(@RequestBody Student student) {
        return ResponseEntity.ok(service.create(student));
    }
}

package com.hobi.dbtest.controller;

import com.hobi.dbtest.entity.Student;
import com.hobi.dbtest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService service;

    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> saveNewStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(student));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Student>> fetchStudents() {
        return ResponseEntity.ok(service.fetchAll());
    }

    @GetMapping(value = "/{student-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> fetchSpecificStudent(@PathVariable("student-id") long studentId) {
        Optional<Student> student = service.fetch(studentId);
        if (student.isPresent())
            return ResponseEntity.ok(student.get());
        else
            return ResponseEntity.notFound().build();
    }
}

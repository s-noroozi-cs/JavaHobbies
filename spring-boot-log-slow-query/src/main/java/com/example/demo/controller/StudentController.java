package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/students")
public class StudentController {

    private StudentRepository repository;

    @Autowired
    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/{student-id}")
    public ResponseEntity<Student> getSpecificStudent(@PathVariable("student-id") Long studentId) {
        if (studentId == null || studentId <= 0)
            return ResponseEntity.badRequest().build();

        Optional<Student> student = repository.findById(studentId);

        if (student.isPresent())
            return ResponseEntity.ok(student.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @SneakyThrows
    public ResponseEntity persistStudent(@RequestBody Student student, HttpServletRequest request) {
        if (student == null || student.getId() != null)
            return ResponseEntity.badRequest().build();

        repository.save(student);

        String studentFetchPath = request.getRequestURL() + "/" + student.getId();

        return ResponseEntity.created(new URI(studentFetchPath)).build();
    }

    @PutMapping(value = "/{student-id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> updateStudent(@PathVariable("student-id") long studentId, @RequestBody Student student) {

        if (student.getId() != null && !student.getId().equals(studentId))
            return ResponseEntity.badRequest().build();

        student.setId(studentId);

        repository.save(student);

        return ResponseEntity.ok(student);
    }

    @DeleteMapping(value = "/{student-id}")
    public ResponseEntity<Student> deleteSpecificStudent(@PathVariable("student-id") Long studentId) {
        if (studentId == null || studentId <= 0)
            return ResponseEntity.badRequest().build();

        Optional<Student> student = repository.findById(studentId);

        if (student.isPresent()) {
            repository.delete(student.get());
            return ResponseEntity.noContent().build();
        } else
            return ResponseEntity.notFound().build();
    }
}

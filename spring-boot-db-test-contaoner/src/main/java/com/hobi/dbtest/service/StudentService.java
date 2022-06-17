package com.hobi.dbtest.service;

import com.hobi.dbtest.entity.Student;
import com.hobi.dbtest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {
    private StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student){
        return repository.save(student);
    }
}

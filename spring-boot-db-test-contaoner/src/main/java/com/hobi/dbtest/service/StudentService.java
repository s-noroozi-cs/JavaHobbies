package com.hobi.dbtest.service;

import com.hobi.dbtest.entity.Student;
import com.hobi.dbtest.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student create(Student student){
        return repository.save(student);
    }

    public List<Student> fetchAll(){
        return repository.findAll();
    }

    public Optional<Student> fetch(long studentId){
        return repository.findById(studentId);
    }
}

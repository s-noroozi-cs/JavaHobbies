package com.example.demo;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.LongStream;

@SpringBootTest
@Slf4j
class DemoApplicationTests {
    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void prepare_test_data() {
        long endRange = 5;
        LongStream
                .rangeClosed(1, endRange)
                .forEach(i -> {
                    repository.save(
                            new Student(i,
                                    "Your name is %d without any things".formatted(i),
                                    (int) i + 10)
                    );
                    if (i % 50 == 0 && i > 0) {
                        repository.flush();
                    }
                });
        Assertions.assertEquals(endRange, repository.count());
    }

    @AfterEach
    void clean_test_data() {
        repository.deleteAll();
        Assertions.assertEquals(0, repository.count());
    }

    @Test
    void check_log_slow_query() {
        PageRequest pageRequest = PageRequest.of(0,100);
        String nameContaining = "10";
        long start = System.currentTimeMillis();
        Page<Student> result= repository
                .findByNameContainingIgnoreCase(nameContaining, pageRequest);
//        Assertions.assertTrue( result.getTotalPages() > 0);
//        long elapseTime = System.currentTimeMillis()-start;
//        log.info("elapse time: %d".formatted(elapseTime));
    }

}

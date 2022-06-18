package com.hobi.dbtest;

import com.hobi.dbtest.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DbTestApplicationTests {

    @LocalServerPort
    private int port;
    private RestTemplate restTemplate = new RestTemplate();

    private String getStudentsUrl() {
        return "http://localhost:" + port + "/api/v1/students";
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_rest_api_call() {
        Student student = new Student();
        student.setName("Ali");
        student.setAge(25);
        ResponseEntity<Student> responseEntity =
                restTemplate.postForEntity(getStudentsUrl(), student, Student.class);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}

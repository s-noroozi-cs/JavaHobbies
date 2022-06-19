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

    private final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;

    private String getStudentsUrl() {
        return "http://localhost:" + port + "/api/v1/students";
    }

    @Test
    void contextLoads() {
    }

    @Test
    void test_create_rest_api_call() {
        Student student = new Student();
        student.setName("Ali");
        student.setAge(25);
        ResponseEntity<Student> responseEntity =
                restTemplate.postForEntity(getStudentsUrl(), student, Student.class);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void test_fetch_all_rest_api_call() {
        ResponseEntity<Student[]> result =
                restTemplate.getForEntity(getStudentsUrl(), Student[].class);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}

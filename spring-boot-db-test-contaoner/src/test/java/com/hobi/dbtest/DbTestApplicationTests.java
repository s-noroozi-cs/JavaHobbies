package com.hobi.dbtest;

import com.hobi.dbtest.entity.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DbTestApplicationTests {

    private static final RestTemplate restTemplate = new RestTemplate();
    @LocalServerPort
    private int port;

    private String getStudentsUrl() {
        return "http://localhost:" + port + "/api/v1/students";
    }

    private String getSpecificStudentUrl(long studentId) {
        return getStudentsUrl() + "/" + studentId;
    }

    @Test
    void contextLoads() {
    }

    ResponseEntity<Student> createNewStudent() {
        Student student = new Student();
        student.setName("Ali");
        student.setAge(25);
        return restTemplate.postForEntity(getStudentsUrl(), student, Student.class);
    }

    ResponseEntity<Student[]> fetchAllStudent() {
        return restTemplate.getForEntity(getStudentsUrl(), Student[].class);
    }

    ResponseEntity<Student> fetchSpecificStudent(long studentId) {
        return restTemplate.getForEntity(getSpecificStudentUrl(studentId), Student.class);
    }


    @Test
    void test_create_rest_api_call() {
        ResponseEntity<Student> responseEntity = createNewStudent();
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void test_fetch_all_rest_api_call() {
        ResponseEntity<Student[]> result = fetchAllStudent();
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void test_fetch_not_found() {
        HttpClientErrorException exception = Assertions.assertThrowsExactly(HttpClientErrorException.NotFound.class,
                () -> fetchSpecificStudent(-1));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void test_fetch_specific_student() {
        ResponseEntity<Student> student = createNewStudent();
        Assertions.assertEquals(HttpStatus.CREATED, student.getStatusCode());
        ResponseEntity<Student> resp = fetchSpecificStudent(student.getBody().getId());
        Assertions.assertEquals(HttpStatus.OK, resp.getStatusCode());
    }

}

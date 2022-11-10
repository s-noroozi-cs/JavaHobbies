package com.jpa.interceptor;

import com.jpa.interceptor.entity.Request;
import com.jpa.interceptor.model.RequestType;
import com.jpa.interceptor.repository.RequestRepository;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.ManyToOne;
import java.lang.reflect.Field;
import java.math.BigDecimal;

@SpringBootTest
class InterceptorApplicationTests {

    @Autowired
    private RequestRepository repository;


    @BeforeEach
    void before_each_tests() {
        Request l1 = new Request();
        l1.setAmount(BigDecimal.ONE);
        l1.setType(RequestType.L1);
        l1.setOriginal(null);
        repository.save(l1);

        Request l2 = new Request();
        l2.setAmount(BigDecimal.ONE);
        l2.setType(RequestType.L2);
        l2.setOriginal(l1);
        repository.save(l2);

        Request l1t = new Request();
        l1t.setAmount(BigDecimal.ONE);
        l1t.setType(RequestType.L1T);
        l1t.setOriginal(l1);
        repository.save(l1t);

        Request l2t = new Request();
        l2t.setAmount(BigDecimal.ONE);
        l2t.setType(RequestType.L2T);
        l2t.setOriginal(l1t);
        repository.save(l2t);
    }

    @AfterEach
    void after_each_tests() {
        repository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void check_original_request_based_on_fetch_type() throws Exception {
        Field field = Request.class.getDeclaredField("original");
        ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        switch (manyToOne.fetch()) {
            case EAGER -> original_request_fetch_type_eager_test();
            case LAZY -> original_request_fetch_type_lazy_test();
            default -> throw new RuntimeException("fetch type does not have any test...");
        }
    }

    void original_request_fetch_type_eager_test() {
        SampleData sampleData = fetchSampleData();
        Assertions.assertNull(sampleData.l1.getOriginal());

        Assertions.assertNotNull(sampleData.l2.getOriginal());

        Assertions.assertNotNull(sampleData.l1t.getOriginal());

        Assertions.assertNotNull(sampleData.l2t.getOriginal());
        Assertions.assertNotNull(sampleData.l2t.getOriginal().getOriginal());
    }

    void original_request_fetch_type_lazy_test() {
        SampleData sampleData = fetchSampleData();

        Assertions.assertNull(sampleData.l1.getOriginal());

        Assertions.assertFalse(Hibernate.isInitialized(sampleData.l2.getOriginal()));

        Assertions.assertFalse(Hibernate.isInitialized(sampleData.l1t.getOriginal()));

        Assertions.assertFalse(Hibernate.isInitialized(sampleData.l2t.getOriginal()));
    }

    private SampleData fetchSampleData() {
        Request l1 = repository.findByType(RequestType.L1).get(0);
        Request l2 = repository.findByType(RequestType.L2).get(0);
        Request l1t = repository.findByType(RequestType.L1T).get(0);
        Request l2t = repository.findByType(RequestType.L2T).get(0);
        return new SampleData(l1, l2, l1t, l2t);
    }

    public record SampleData(Request l1, Request l2, Request l1t, Request l2t) {
    }
}

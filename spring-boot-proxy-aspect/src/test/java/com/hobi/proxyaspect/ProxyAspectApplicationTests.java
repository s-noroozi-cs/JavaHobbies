package com.hobi.proxyaspect;

import com.hobi.proxyaspect.logging.MemoryAppenderInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProxyAspectApplicationTests {

    private RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    int randomServerPort;

    private String getHostUrl(){
        return String.format("http://localhost:%d",randomServerPort);
    }

    @Test
    void context_loads() {
        Assertions.assertTrue(MemoryAppenderInstance.getInstance().getEvents().size() > 0);
    }

    @Test
    void test_interceptor_logs(){
        restTemplate.getForEntity(getHostUrl() + "/uuid",String.class);
        MemoryAppenderInstance.getInstance().getEvents()
                .stream()
                .map(i -> String.format("time: %d, logger:%s, message: %s"
                        ,i.getTimeStamp(),i.getLoggerName(),i.getMessage()))
                .forEach(System.out::println);
    }

}

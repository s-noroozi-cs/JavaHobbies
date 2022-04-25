package com.hobi.proxyaspect;

import com.hobi.proxyaspect.logging.MemoryAppender;
import com.hobi.proxyaspect.logging.MemoryAppenderInstance;
import com.hobi.proxyaspect.util.LogUtil;
import org.junit.jupiter.api.*;
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

    @AfterAll
    static void logging_events(){
        MemoryAppenderInstance.getInstance().getEvents().stream().forEach(System.out::println);
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
                .filter(i -> i.getLoggerName().equals(LogUtil.class.getName()))
                .min((a,b) -> (int) (a.getTimeStamp() - b.getTimeStamp()))
                .filter(i -> i.getMessage().contains("source:AuthorizationInterceptor,"))
                .orElseThrow(()-> new RuntimeException("Interceptor log should be logged before all others"));

        MemoryAppenderInstance.getInstance().getEvents()
                .stream()
                .filter(i -> i.getLoggerName().equals(LogUtil.class.getName()))
                .max((a,b) -> (int) (a.getTimeStamp() - b.getTimeStamp()))
                .filter(i -> i.getMessage().contains("source:AuthorizationAspect,"))
                .orElseThrow(()-> new RuntimeException("Authorization aspect log should be logged after all others"));

    }



}

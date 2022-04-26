package com.hobi.proxyaspect;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.hobi.proxyaspect.logging.MemoryAppenderInstance;
import com.hobi.proxyaspect.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Comparator;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProxyAspectApplicationTests {

    private RestTemplate restTemplate = new RestTemplate();

    @LocalServerPort
    int randomServerPort;

    private String getHostUrl() {
        return String.format("http://localhost:%d", randomServerPort);
    }

    @AfterEach
    void logging_events() {
        MemoryAppenderInstance.getInstance().getEvents().stream().forEach(System.out::println);
        MemoryAppenderInstance.getInstance().clear();
    }

    private void check_biz_log(Comparator<ILoggingEvent> minComparator, String logMsg, String description) {
        MemoryAppenderInstance.getInstance().getEvents()
                .stream()
                .filter(i -> i.getLoggerName().equals(LogUtil.class.getName()))
                .min(minComparator)
                .filter(i -> i.getMessage().contains(logMsg))
                .orElseThrow(() -> new RuntimeException(description));
    }

    @Test
    void test_interceptor_then_aspect_logs_without_bean_validation() {
        restTemplate.getForEntity(getHostUrl() + "/uuid", String.class);
        check_biz_log((a, b) -> (int) (a.getTimeStamp() - b.getTimeStamp())
                , "source:AuthorizationInterceptor,"
                , "Interceptor log should be logged before all others");

        check_biz_log((a, b) -> (int) (b.getTimeStamp() - a.getTimeStamp()),
                "source:AuthorizationAspect,"
                , "Authorization aspect log should be logged after all others");
    }

    @Test
    void test_interceptor_logs_with_bean_validation() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json");
        HttpEntity<String> request = new HttpEntity<>("{}", headers);
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //nop
            }

            @Override
            public void handleError(URI url, HttpMethod method, ClientHttpResponse response) throws IOException {
                //nop
            }
        });
        ResponseEntity<String> resp = restTemplate
                .postForEntity(getHostUrl() + "/sayHello", request, String.class);
        Assertions.assertEquals(400, resp.getStatusCode().value());

    }


}

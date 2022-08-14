package com.example.asyncservlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.example.asyncservlet.HeaderNames.REQUEST_UNIQUE_ID;

@Component
public class AsyncRequestListener {

    private StreamBridge streamBridge;

    @Autowired
    public AsyncRequestListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Bean
    Consumer<Message<String>> input() {
        return message -> {
            String requestUniqueId = message.getHeaders().get(REQUEST_UNIQUE_ID, String.class);
            String payload = LocalDateTime.now() + "_" + message.getPayload();
            try {
                AsyncContext asyncContext = AsyncContextHolder.getAndRemove(requestUniqueId);
                HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
                response.addHeader(REQUEST_UNIQUE_ID, requestUniqueId);
                response.getWriter().println(payload);
                asyncContext.complete();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

        };
    }

}

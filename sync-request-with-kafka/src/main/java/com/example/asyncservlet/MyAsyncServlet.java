package com.example.asyncservlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.example.asyncservlet.HeaderNames.REQUEST_UNIQUE_ID;

public class MyAsyncServlet extends HttpServlet {

    private StreamBridge streamBridge;

    @Autowired
    public MyAsyncServlet(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("payload");
        if(id == null || id.isBlank())
            id = "payload is null or blank";
        String requestUniqueId = UUID.randomUUID().toString();
        streamBridge.send(Source.OUTPUT, MessageBuilder.withPayload(id.getBytes(StandardCharsets.UTF_8))
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.TEXT_PLAIN)
                .setHeader(REQUEST_UNIQUE_ID, requestUniqueId)
                .build());
        req.startAsync();
        AsyncContext asyncContext = req.getAsyncContext();
        asyncContext.setTimeout(5_000L);
        AsyncContextHolder.addContext(requestUniqueId,asyncContext);
    }
}

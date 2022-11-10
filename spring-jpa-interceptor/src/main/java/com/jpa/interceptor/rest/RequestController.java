package com.jpa.interceptor.rest;

import com.jpa.interceptor.entity.Request;
import com.jpa.interceptor.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private RequestRepository repository;

    @Autowired
    public RequestController(RequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/{request-id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Request> getRequest(@PathVariable("request-id") long requestId) {
        if(requestId <=0 )
            return ResponseEntity.badRequest().build();

        Optional<Request> result = repository.findById(requestId);
        if(result.isPresent())
            return ResponseEntity.ok(result.get());
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Request> saveRequest(@RequestBody Request request, HttpServletRequest httpRequest){
        if(request.getId() != null)
            return ResponseEntity.badRequest().build();
        repository.save(request);
        return ResponseEntity.created(URI.create(httpRequest.getRequestURL() +  "/" + request.getId())).build();
    }
}

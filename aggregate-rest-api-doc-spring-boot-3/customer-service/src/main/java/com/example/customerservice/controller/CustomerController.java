package com.example.customerservice.controller;

import com.example.customerservice.model.Customer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {


    @RequestMapping(method = RequestMethod.GET
            , path = "/{customer-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> get(@PathVariable("customer-id") Long customerId) {
        return ResponseEntity.ok().build();
    }


    @RequestMapping(method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("/?")).build();
    }

    @RequestMapping(method = RequestMethod.PUT
            , path = "/{customer-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> update(@PathVariable("customer-id") Long customerId
            , @RequestBody Customer customer) {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE
            , path = "/{customer-id}")
    public ResponseEntity delete(@PathVariable("customer-id") Long customerId) {
        return ResponseEntity.noContent().build();
    }

}

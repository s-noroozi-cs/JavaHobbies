package com.example.customerservice.controller;

import com.example.customerservice.model.Customer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {


    @RequestMapping(method = RequestMethod.GET
            , path = "/{customer-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> get(@PathVariable("customer-id") Long customerId) {
    return ResponseEntity.ok(
        Customer.builder()
            .address("address-" + customerId)
            .name("name-" + customerId)
            .phoneNumber("phoneNumber-" + customerId)
            .enable(true)
            .build());
    }

}

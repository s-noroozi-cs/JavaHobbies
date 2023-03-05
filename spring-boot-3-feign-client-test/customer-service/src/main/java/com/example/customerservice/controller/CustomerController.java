package com.example.customerservice.controller;

import com.example.customerservice.client.MerchantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private MerchantClient merchantClient;

    @RequestMapping(method = RequestMethod.GET,
            path = "/test/merchants/{merchant-id}")
    public ResponseEntity delete(@PathVariable("merchant-id") Long merchantId) {
        return merchantClient.get(merchantId);
    }

}

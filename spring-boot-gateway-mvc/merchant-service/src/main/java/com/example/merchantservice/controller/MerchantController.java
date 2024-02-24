package com.example.merchantservice.controller;

import com.example.merchantservice.model.Merchant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {


    @RequestMapping(method = RequestMethod.GET
            , path = "/{merchant-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Merchant> get(@PathVariable("merchant-id") Long merchantId) {
    return ResponseEntity.ok(
        Merchant.builder()
            .code("code-" + merchantId)
            .name("name-" + merchantId)
            .address("address-" + merchantId)
            .build());
    }
}

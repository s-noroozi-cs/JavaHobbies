package com.example.merchantservice.controller;

import com.example.merchantservice.model.Merchant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {


    @RequestMapping(method = RequestMethod.GET
            , path = "/{merchant-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Merchant> get(@PathVariable("merchant-id") Long merchantId) {
        return ResponseEntity.ok().build();
    }


    @RequestMapping(method = RequestMethod.POST
            , consumes = MediaType.APPLICATION_JSON_VALUE
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@RequestBody Merchant merchant) {
        return ResponseEntity.created(URI.create("/?")).build();
    }

    @RequestMapping(method = RequestMethod.PUT
            , path = "/{merchant-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Merchant> update(@PathVariable("merchant-id") Long merchantId
            , @RequestBody Merchant merchant) {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE
            , path = "/{merchant-id}")
    public ResponseEntity delete(@PathVariable("merchant-id") Long merchantId) {
        return ResponseEntity.noContent().build();
    }

}

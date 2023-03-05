package com.example.customerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@FeignClient(name = "merchant")
public interface MerchantClient {

    @RequestMapping(method = RequestMethod.GET
            , path = "/api/v1/merchants/{merchant-id}"
            , produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity get(@PathVariable("merchant-id") Long merchantId);

    @RequestMapping(method = RequestMethod.DELETE
            , path = "/api/v1/merchants/{merchant-id}")
    ResponseEntity delete(@PathVariable("merchant-id") Long merchantId);

}

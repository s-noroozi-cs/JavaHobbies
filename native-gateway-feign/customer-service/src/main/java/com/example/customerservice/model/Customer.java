package com.example.customerservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class Customer {
    private String name;
    private String phoneNumber;
    private String address;
    private boolean enable;
}

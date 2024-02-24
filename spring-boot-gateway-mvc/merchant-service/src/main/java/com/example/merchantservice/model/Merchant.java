package com.example.merchantservice.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class Merchant {
    private String name;
    private String code;
    private String address;
}

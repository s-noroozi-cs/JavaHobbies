package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class SampleEntity {
    @Id
    private Long id;
}

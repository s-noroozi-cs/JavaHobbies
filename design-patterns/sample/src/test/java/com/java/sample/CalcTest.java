package com.java.sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalcTest {
    @Test
    void a() {
        Assertions.assertEquals(4, new Calc().add(1, 3));
    }
}

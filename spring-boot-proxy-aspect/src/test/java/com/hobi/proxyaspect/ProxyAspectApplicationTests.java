package com.hobi.proxyaspect;

import com.hobi.proxyaspect.logging.MemoryAppenderInstance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProxyAspectApplicationTests {

    @Test
    void contextLoads() {
        Assertions.assertTrue(MemoryAppenderInstance.getInstance().getEvents().size() > 0);
    }

}

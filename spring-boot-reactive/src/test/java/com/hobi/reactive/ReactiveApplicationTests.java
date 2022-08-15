package com.hobi.reactive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveApplicationTests {

	@LocalServerPort
	private int port;

	@Test
	void contextLoads() {
	}

	@Test
	void check_server_port(){
		Assertions.assertTrue(port > 0);
	}

}

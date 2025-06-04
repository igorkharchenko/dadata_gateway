package com.hatand.dadatagateway.framework;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
class DaDataGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

}

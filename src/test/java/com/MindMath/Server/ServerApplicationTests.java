package com.MindMath.Server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.mocah.mindmath.server.ServerApplication;


@SpringBootTest
@ContextConfiguration(classes =ServerApplication.class)
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

}

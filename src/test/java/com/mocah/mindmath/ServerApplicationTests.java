package com.mocah.mindmath;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mocah.mindmath.server.ServerApplication;
import com.mocah.mindmath.server.controller.cabri.Taskcontroller;

@SpringBootTest(classes=ServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
public class ServerApplicationTests {

	@Autowired
	public Taskcontroller controller;

	@Test
	public void contexLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

}

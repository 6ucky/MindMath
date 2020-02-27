package com.mocah.mindmath.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mocah.mindmath.learning.MainLearningProcess;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		MainLearningProcess.main(args);
	}

}

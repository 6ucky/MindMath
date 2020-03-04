package com.mocah.mindmath.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.mocah.mindmath.learning.MainLearningProcess;

@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer  {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServerApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
		MainLearningProcess.main(args);
	}

}

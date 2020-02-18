package com.MindMath.Server.cabri;

import org.springframework.web.bind.annotation.*;

@RestController
public class Taskcontroller {

	@PostMapping(path = "/task", consumes = "application/json")
	public String addtask(@RequestBody Task data) {
		String message = "";
		message += "id:" + data.getId() + "\n" + "task:" + data.getTask() + "\n" + "trigger:" + data.getTrigger() + "\n"
				+ "VT-2.1:" + data.getVT_2_1() + "\n" + "VT-2.2:" + data.getVT_2_2() + "\n"
				+ "VT-2.3:" + data.isVT_2_3() + "\n" + "VT-2.4:" + data.isVT_2_4() + "\n";
		System.out.println(message);
		
		return "JSON file received by the server.";
	}
}
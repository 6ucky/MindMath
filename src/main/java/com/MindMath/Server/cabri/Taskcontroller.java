package com.MindMath.Server.cabri;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Taskcontroller {

	@Autowired
	private Taskrepository taskrepository;
	
	@PostMapping(path = "/task", consumes = "application/json")
	public String addtask(@RequestBody Task data) {
		
		if(!taskrepository.existsById(data.getId()))
		{
			taskrepository.save(data);
			System.out.println("Saved!\n");
		}
		String message = "";
		message += "id:" + data.getId() + "\n" + "task:" + data.getTask() + "\n" + "trigger:" + data.getTrigger() + "\n"
				+ "VT-2.1:" + data.getVT_2_1() + "\n" + "VT-2.2:" + data.getVT_2_2() + "\n"
				+ "VT-2.3:" + data.isVT_2_3() + "\n" + "VT-2.4:" + data.isVT_2_4() + "\n";
		System.out.println(message);
		
		return "JSON file received by the server.";
	}
	
	@GetMapping("/task")
	public List<Task> getALLtask() {
		
		List<Task> tasks = new ArrayList<>();
		taskrepository.findAll()
		.forEach(tasks::add);
		
		return tasks;
	}
	
	@PutMapping(path = "/task")
	public String updatetask(@RequestBody Task data) {
		taskrepository.save(data);
		//Q-learning algorithm
		return "Task is updated. Q-learning algorithm is applied.";
	}
}
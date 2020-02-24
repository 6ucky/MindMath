package com.MindMath.Server.cabri;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@RestController
public class Taskcontroller {

	@Autowired
	private Taskrepository taskrepository;

	@PostMapping(path = "/task", consumes = "application/json")
	public String addtask(@RequestBody Task data) {
		if (!taskrepository.existsById(data.getId())) {
			taskrepository.save(data);
			System.out.println("Saved!\n");
		}

		// TODO call Q-learning algorithm

		return "JSON file received by the server.";
	}

	@GetMapping("/task")
	public List<Task> getALLtask() {
		List<Task> tasks = new ArrayList<>();
		taskrepository.findAll().forEach(tasks::add);

		return tasks;
	}

	@PutMapping(path = "/task")
	public String updatetask(@RequestBody Task data) {
		taskrepository.save(data);

		return "Task is updated. Q-learning algorithm is applied.";
	}
	
	@DeleteMapping(path = "/task")
	public void cleandatabase() {
		taskrepository.deleteAll();
	}
}
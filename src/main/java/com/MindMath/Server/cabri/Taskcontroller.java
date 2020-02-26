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
	
	/**
	 * Handle POST request
	 * @param data Received JSON file mapping to task class
	 * @return feedback message
	 */
	@PostMapping(path = "/task", consumes = "application/json")
	public String addtask(@RequestBody Task data) {
		if (!getTaskrepository().existsById(data.getId())) {
			getTaskrepository().save(data);
			System.out.println("Saved!\n");
		}

		// TODO call Q-learning algorithm

		return "JSON file received by the server.";
	}

	/**
	 * Handle GET request
	 * @return all the tasks in the repository
	 */
	@GetMapping("/task")
	public List<Task> getALLtask() {
		List<Task> tasks = new ArrayList<>();
		getTaskrepository().findAll().forEach(tasks::add);

		return tasks;
	}

	/**
	 * Handle PUT request, update task based on ID
	 * @param data Received JSON file mapping to task class
	 * @return feedback message
	 */
	@PutMapping(path = "/task")
	public String updatetask(@RequestBody Task data) {
		getTaskrepository().save(data);

		return "Task is updated. Q-learning algorithm is applied.";
	}
	
	/**
	 * Handle DELETE request
	 */
	@DeleteMapping(path = "/task")
	public void cleandatabase() {
		getTaskrepository().deleteAll();
	}

	/**
	 * get task repository
	 * @return a repository interface that works with task entities
	 */
	public Taskrepository getTaskrepository() {
		return taskrepository;
	}
}
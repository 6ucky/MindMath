package com.mocah.mindmath.server.cabri;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mocah.mindmath.learning.exceptions.JsonParserException;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepository;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@RestController
public class Taskcontroller {

	@Autowired
	private Taskrepository taskrepository;
	private final String license_num = "mocah";
	
	/**
	 * Handle POST request
	 * @param version default version is 1.0
	 * @param data Receive JSON file as string
	 * @return feedback message
	 * @throws JsonParserException 
	 */
	@PostMapping(path = "/task", consumes = "application/json")
	public ResponseEntity<String> addtask(@RequestHeader("Version-LIP6") String version, @RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserException {
		if(!auth.contains(license_num))
		{
			return new ResponseEntity<String>("Unauthorized.", HttpStatus.UNAUTHORIZED);
		}
		ParserFactory<Task> jsonparser = new JsonParserFactory(data);
		Task tasks = jsonparser.parse(data);
		if (!getTaskrepository().existsById(tasks.getId())) {
			getTaskrepository().save(tasks);
			System.out.println("Saved!\n");
		}

		// TODO call Q-learning algorithm

		return new ResponseEntity<String>("JSON file received by the server.", HttpStatus.CREATED);
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
	 * Default GET request
	 * @return server started
	 */
	@GetMapping("/")
	public String home() {
		return "The server is started.";
	}
	
	/**
	 * Send get request to Learning Locker
	 * @return the message from Learning Locker
	 */
	@GetMapping("/ll")
	public String ll(){
		LearningLockerRepository ll = new LearningLockerRepository();
		return ll.getfromLearningLocker();
	}
	
	/**
	 * Handle PUT request, update task based on ID
	 * @param data Received JSON file mapping to task class
	 * @return feedback message
	 * @throws JsonParserException 
	 */
	@PutMapping(path = "/task")
	public String updatetask(@RequestBody String data) throws JsonParserException {
		ParserFactory<Task> jsonparser = new JsonParserFactory(data);
		Task tasks = jsonparser.parse(data);
		getTaskrepository().save(tasks);

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
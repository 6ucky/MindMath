package com.mocah.mindmath.server.cabri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@RestController
@RequestMapping("/task")
public class Taskcontroller {
	
	@Autowired
	private Taskrepository taskrepository;
	
	private static final String license_num = "mocah";
	
	/**
	 * check the post request based on authorization
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if(auth.equals(license_num))
			return true;
		return false;
	}
	
	/**
	 * Handle POST request default version is 1.0
	 */
	@PostMapping(path = "/", consumes = "application/json")
	public ResponseEntity<String> addtask(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException, IOException {
		return addtaskv1_0(auth, data);
	}
	
	/**
	 * Handle POST request in version 1.0
	 * @param data Receive JSON file as string
	 * @param auth authorization headers
	 * @return feedback message
	 * @throws IOException 
	 * @throws JsonParseCustomException 
	 */
	@PostMapping(path = "/v1.0", consumes = "application/json")
	public ResponseEntity<String> addtaskv1_0(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException, IOException {
		if(!checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		jsonparser.getValueAsString(jsonparser.getObject(), JsonParserKeys.TASK_ID);
		Task tasks = jsonparser.parse(data, "v1.0");
		
		
		if (!getTaskrepository().existsById(tasks.getId())) {
			getTaskrepository().save(tasks);
			
			// TODO call Q-learning algorithm
			
		}
		
		Feedbackjson responsejson = new Feedbackjson(jsonparser.getValueAsString(jsonparser.getObject(), JsonParserKeys.TASK_ID));
		Gson gson = new Gson();
		return new ResponseEntity<String>(gson.toJson(responsejson), HttpStatus.OK);
	}

	/**
	 * Handle GET request
	 * @return all the tasks in the repository
	 */
	@GetMapping("/")
	public ResponseEntity<String> getALLtask(@RequestHeader("Authorization") String auth) {
		if(!auth.equals("test"))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		List<Task> tasks = new ArrayList<>();
		getTaskrepository().findAll().forEach(tasks::add);
		if(tasks.size() == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
		Gson gson = new Gson();
		return new ResponseEntity<String>(gson.toJson(tasks), HttpStatus.FOUND);
	}
	
	/**
	 * Handle DELETE request
	 * @return 
	 */
	@DeleteMapping(path = "/")
	public ResponseEntity<String> cleandatabase(@RequestHeader("Authorization") String auth) {
		if(!auth.equals("test"))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		getTaskrepository().deleteAll();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
	}

	/**
	 * get task repository
	 * @return a repository interface that works with task entities
	 */
	public Taskrepository getTaskrepository() {
		return taskrepository;
	}
}
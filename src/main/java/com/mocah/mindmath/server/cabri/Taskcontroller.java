package com.mocah.mindmath.server.cabri;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
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

import com.google.gson.Gson;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.decisiontree.search.DeepFirstSearch;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserLogs;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.parser.owlparser.OWLAPIparser;
import com.mocah.mindmath.repository.Matrixrepository;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepository;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@RestController
public class Taskcontroller {

	@Autowired
	private Taskrepository taskrepository;
	private static final String license_num = "mocah";
	private static final String test_url = "mindmath.lip6.fr/videos/ResolutionEquation.mp4";
	
	/**
	 * check the post request based on authorization
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	public static boolean checkauth(String auth) {
		if(auth.equals(license_num))
			return true;
		return false;
	}
	
	/**
	 * Handle POST request
	 * @param version default version is 1.0
	 * @param data Receive JSON file as string
	 * @param auth authorization headers
	 * @return feedback message
	 * @throws JsonParserException 
	 */
	@PostMapping(path = "/task", consumes = "application/json")
	public ResponseEntity<String> addtask(@RequestHeader("Version-LIP6") String version, @RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserException {
		if(!checkauth(auth))
		{
			return new ResponseEntity<String>("Unauthorized connection.", HttpStatus.UNAUTHORIZED);
		}
		
		Task tasks = JsonParserFactory.parseCabri(data);
		
		if (!getTaskrepository().existsById(tasks.getId())) {
			getTaskrepository().save(tasks);
			
			// TODO call Q-learning algorithm
			
//			return new ResponseEntity<String>("JSON file is saved in the server.", HttpStatus.CREATED);
		}

//		return new ResponseEntity<String>("Duplicated JSON file found in the server.", HttpStatus.CONFLICT);
		Feedbackjson responsejson = new Feedbackjson(tasks.getId(), test_url);
		Gson gson = new Gson();
		return new ResponseEntity<String>(gson.toJson(responsejson), HttpStatus.FOUND);
	}

	/**
	 * Handle GET request
	 * @return all the tasks in the repository
	 */
	@GetMapping("/task")
	public ResponseEntity<String> getALLtask() {
		List<Task> tasks = new ArrayList<>();
		getTaskrepository().findAll().forEach(tasks::add);
		if(tasks.size() == 0)
			return new ResponseEntity<String>("Database is empty.", HttpStatus.NOT_FOUND);
		Gson gson = new Gson();
		return new ResponseEntity<String>(gson.toJson(tasks), HttpStatus.FOUND);
	}
	
	/**
	 * Send get request to Learning Locker
	 * @return the message from Learning Locker
	 */
	@GetMapping("/ll")
	public ResponseEntity<String> getAboutLearningLocker(){
		LearningLockerRepository ll = new LearningLockerRepository();
		return new ResponseEntity<String>(ll.getAllStatementfromLearningLocker(), HttpStatus.ACCEPTED);
	}
	
	/**
	 * Test of post for LRS
	 * @return the added statement
	 */
	@PostMapping("/lltest")
	public ResponseEntity<String> testLearningLocker(@RequestBody String data){
		LearningLockerRepository ll = new LearningLockerRepository();
		JsonParserLogs parserLog = new JsonParserLogs(data);
		JsonParserSensor parserSensor = new JsonParserSensor(data);
		return new ResponseEntity<String>(ll.postStatementTEST(parserSensor.getTaskId(), parserSensor.getSensor(), parserLog.getLogs()), HttpStatus.ACCEPTED);
	}
	
	/**
	 * Send OWL file
	 * @return the full list of statement information including resource, property and RDF node.
	 */
	@PostMapping("/ontology")
	public ResponseEntity<String> getresourceontology(@RequestBody String data){
		Gson gson = new Gson();
		OWLAPIparser owlparser = new OWLAPIparser(data);
		return new ResponseEntity<String>(gson.toJson(owlparser.listall()), HttpStatus.ACCEPTED);
	}
	
	/**	
	 * receive JSON and update the decision tree class
	 * @param version default version is 1.0
	 * @param auth authorization headers
	 * @param data receive JSON file as String
	 * @return HTTP status and message
	 */
	@PostMapping("/matrix")
	public ResponseEntity<String> addmatrix(@RequestHeader("Version-LIP6") String version, @RequestHeader("Authorization") String auth,
			@RequestBody String data) {
		if(!checkauth(auth))
		{
			return new ResponseEntity<String>("Unauthorized connection.", HttpStatus.UNAUTHORIZED);
		}
		Gson gson = new Gson();
		Tree tree = gson.fromJson(data, Tree.class);
		Node root = tree.getRoot();
		DeepFirstSearch dfs = new DeepFirstSearch(tree);
		dfs = Matrixrepository.getVisitedNode(dfs, root);
		
		return new ResponseEntity<String>("Matrix is updated in the server.\nVisit order: " + dfs.getVisitedNodes(), HttpStatus.CREATED);
	}
	
	/**
	 * @deprecated use post to update JSON file
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
	 * @return 
	 */
	@DeleteMapping(path = "/task")
	public ResponseEntity<String> cleandatabase() {
		getTaskrepository().deleteAll();
		return new ResponseEntity<String>("Database is empty.", HttpStatus.NOT_FOUND);
	}

	/**
	 * get task repository
	 * @return a repository interface that works with task entities
	 */
	public Taskrepository getTaskrepository() {
		return taskrepository;
	}
}
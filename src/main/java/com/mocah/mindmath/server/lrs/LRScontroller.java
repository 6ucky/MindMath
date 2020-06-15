package com.mocah.mindmath.server.lrs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.jsonparser.JsonParserLogs;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepository;
import com.mocah.mindmath.repository.learninglocker.XAPIgenerator;
import com.mocah.mindmath.repository.learninglocker.XAPItype;

/**
 * @author	Yan Wang
 */

@RestController
@RequestMapping("/lrs")
public class LRScontroller {

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
	 * Send get request to Learning Locker
	 * @return the message from Learning Locker
	 */
	@GetMapping("/all")
	public ResponseEntity<String> getAboutLearningLocker(){
		LearningLockerRepository ll = new LearningLockerRepository();
		return new ResponseEntity<String>(ll.getAllStatementfromLearningLocker(), HttpStatus.ACCEPTED);
	}
	
	/**
	 * Test of post for LRS
	 * @return the added statement
	 * @throws JsonParseCustomException 
	 */
	@PostMapping("/test")
	public ResponseEntity<String> testLearningLocker(@RequestBody String data, @RequestHeader("Authorization") String auth) throws JsonParserCustomException{
		if(checkauth(auth))
		{
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		}
		LearningLockerRepository ll = new LearningLockerRepository();
		JsonParserLogs parserLog = new JsonParserLogs(data);
		JsonParserSensor parserSensor = new JsonParserSensor(data);
		JsonParserFactory parserRoot = new JsonParserFactory(data);
		
		JsonObject response = new JsonObject();
		response.add("Response from LRS", JsonParser.parseString(ll.postStatementTEST(parserRoot.getValueAsString(parserRoot.getObject(), JsonParserKeys.TASK_ID), parserSensor.getSensor(), parserLog.getLogs())).getAsJsonObject());
		
		JsonArray statementArray = new JsonArray();
		XAPIgenerator xapi = new XAPIgenerator();
		String student_id = parserRoot.getValueAsString(parserRoot.getObject(), JsonParserKeys.TASK_ID);
		xapi.setActor("student-" + student_id + "@lip6.fr", "student-" + student_id);
		xapi.setVerb();
		
		xapi.setObject(JsonParserKeys.SENSOR_DOMAIN, parserSensor.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_DOMAIN));
		xapi.setContext(XAPItype.SENSORS);
		statementArray.add(xapi.generateStatement());
		
		xapi.setObject(JsonParserKeys.SENSOR_GENERATOR, parserSensor.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_GENERATOR));
		xapi.setContext(XAPItype.SENSORS);
		statementArray.add(xapi.generateStatement());
		
		for(int i = 0; i < parserLog.getArray().size(); i++)
		{
			xapi = new XAPIgenerator();
			xapi.setActor("student-" + student_id + "@lip6.fr", "student-" + student_id);
			xapi.setVerb();
			xapi.setObject(i, parserLog.getValueAsString(parserLog.getArray().get(i).getAsJsonObject(), JsonParserKeys.LOG_ACTION), parserLog.getValueAsString(parserLog.getArray().get(i).getAsJsonObject(), JsonParserKeys.LOG_NAME));
			xapi.setContext(XAPItype.LOGS);
			statementArray.add(xapi.generateStatement());
		}
		
		response.add("Statements Drafts", statementArray);
		
		return new ResponseEntity<String>(response.toString(), HttpStatus.ACCEPTED);
	}
}

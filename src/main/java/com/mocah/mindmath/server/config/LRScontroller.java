package com.mocah.mindmath.server.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.jsonparser.JsonParserLogs;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryAggregation;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryHttp;
import com.mocah.mindmath.repository.learninglocker.XAPIgenerator;
import com.mocah.mindmath.repository.learninglocker.XAPItype;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.entity.task.Task;

import gov.adlnet.xapi.model.Account;
import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.ContextActivities;
import gov.adlnet.xapi.model.InteractionComponent;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.Verbs;

/**
 * @author Yan Wang
 */

@RestController
@RequestMapping("/lrs")
public class LRScontroller {

	private static final String license_num = "mocah";

	private Statement statement;

	/**
	 * check the post request based on authorization
	 *
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if (auth.equals(license_num))
			return true;
		return false;
	}

	/**
	 * Send get request to Learning Locker
	 *
	 * @return the message from Learning Locker
	 */
	@GetMapping("/all")
	public ResponseEntity<String> getAllLearningLocker() {
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp();
		StatementResult statements = ll.getAllStatements();
		return new ResponseEntity<>(statements.getStatements().toString(), HttpStatus.ACCEPTED);
	}
	
	//test to get all statements from MindMath test store
	@GetMapping("/test/all")
	public ResponseEntity<String> getTestAllLearningLocker() {
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(true);
		StatementResult statements = ll.getAllStatements();
		return new ResponseEntity<>(statements.getStatements().toString(), HttpStatus.ACCEPTED);
	}

	//test to get filters statements from MindMath test store
	@GetMapping("/test/filters")
	public ResponseEntity<String> getFilterLearningLocker() throws UnsupportedEncodingException {
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(true);
		Agent agent = new Agent();
		Account account = new Account("100", "https://www.tralalere.com/");
		agent.setAccount(account);
		StatementResult result = ll.filterByActor(agent).includeRelatedAgents(true).limitResults(10).canonical()
				.getFilteredStatements();
		return new ResponseEntity<>(result.getStatements().toString(), HttpStatus.ACCEPTED);
	}
	
	//test to get aggregation statements from MindMath test store
	@GetMapping("/test/aggregation")
	public ResponseEntity<String> getAggregationLearningLocker() {
		LearningLockerRepositoryAggregation lltest = new LearningLockerRepositoryAggregation(true);
		StatementResult result = lltest.addPipelineStage("context.extensions.yourExtension", "customExtensionValue").getFilteredStatements();
		return new ResponseEntity<>(result.getStatements().toString(), HttpStatus.ACCEPTED);
	}

	/**
	 * Test of post for LRS
	 * @deprecated use jxapi instead
	 *
	 * @return the added statement
	 * @throws JsonParseCustomException
	 */
	@PostMapping("/test")
	public ResponseEntity<String> testLearningLocker(@RequestBody String data,
			@RequestHeader("Authorization") String auth) throws JsonParserCustomException {
		if (checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp();
		JsonParserLogs parserLog = new JsonParserLogs(data);
		JsonParserSensor parserSensor = new JsonParserSensor(data);
		JsonParserFactory parserRoot = new JsonParserFactory(data);

		JsonObject response = new JsonObject();
		response.add("Response from LRS",
				JsonParser.parseString(ll.postStatementTEST(
						parserRoot.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_LEARNER_ID),
						parserSensor.getSensor(), parserLog.getLogs())).getAsJsonObject());

		JsonArray statementArray = new JsonArray();
		XAPIgenerator xapi = new XAPIgenerator();
		String student_id = parserRoot.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_LEARNER_ID);
		xapi.setActor("student-" + student_id + "@lip6.fr", "student-" + student_id);
		xapi.setVerb();

		xapi.setObject(JsonParserKeys.SENSOR_DOMAIN,
				parserSensor.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_DOMAIN));
		xapi.setContext(XAPItype.SENSORS);
		statementArray.add(xapi.generateStatement());

		xapi.setObject(JsonParserKeys.SENSOR_GENERATOR,
				parserSensor.getValueAsString(parserSensor.getObject(), JsonParserKeys.SENSOR_GENERATOR));
		xapi.setContext(XAPItype.SENSORS);
		statementArray.add(xapi.generateStatement());

		for (int i = 0; i < parserLog.getArray().size(); i++) {
			xapi = new XAPIgenerator();
			xapi.setActor("student-" + student_id + "@lip6.fr", "student-" + student_id);
			xapi.setVerb();
			xapi.setObject(i,
					parserLog.getValueAsString(parserLog.getArray().get(i).getAsJsonObject(),
							JsonParserKeys.LOG_ACTION),
					parserLog.getValueAsString(parserLog.getArray().get(i).getAsJsonObject(), JsonParserKeys.LOG_NAME));
			xapi.setContext(XAPItype.LOGS);
			statementArray.add(xapi.generateStatement());
		}

		response.add("Statements Drafts", statementArray);

		return new ResponseEntity<>(response.toString(), HttpStatus.ACCEPTED);
	}

	// jxapi template
	@PostMapping("/test/JXAPI")
	public ResponseEntity<String> testJXAPI(@RequestBody String data, @RequestHeader("Authorization") String auth)
			throws IOException, NoSuchAlgorithmException, URISyntaxException {
		// Statement
		Statement statement = new Statement();

		statement.setId(UUID.randomUUID().toString());

		// Agent
		Agent agent = new Agent();
//		agent.setMbox("mailto:test@example.com");
//		agent.setName("Tester McTesterson");
		String name_account = "Test Name";
		String homepage_account = "http://test.com";
		Account account = new Account(name_account, homepage_account);
		agent.setAccount(account);
		statement.setActor(agent);

		// Verb
		Verb verb = Verbs.experienced();
		statement.setVerb(verb);

		// Activity
		Activity a = new Activity();
		a.setId("http://example.com");
		statement.setObject(a);

		// ActivityDefinition
		String key = "en-US";
		String name = "Unit Testing";
		String description = "Unit testing activity definitions.";
		HashMap<String, String> nameMap = new HashMap<>();
		HashMap<String, String> descriptionMap = new HashMap<>();
		nameMap.put(key, name);
		descriptionMap.put(key, description);
		ActivityDefinition activityDefinition = new ActivityDefinition(nameMap, descriptionMap);
		String moreInfo = "More unit testing information.";
		activityDefinition.setMoreInfo(moreInfo);
		String type = "http://example.com/activities/unittest";
		activityDefinition.setType(type);
		HashMap<String, JsonElement> extensions = new HashMap<>();
		key = "http://example.com/testJSONprimitive";
		extensions.put(key, new JsonPrimitive(44));
		JsonObject jo = new JsonObject();
		jo.addProperty("http://example.com/unitTest", "unit test");
		key = "http://example.com/testJSONobject";
		extensions.put(key, jo);
		activityDefinition.setExtensions(extensions);
		String interactionType = "performance";
		activityDefinition.setInteractionType(interactionType);
		ArrayList<String> correctResponsesPattern = new ArrayList<>();
		correctResponsesPattern.add("true");
		correctResponsesPattern.add("foo");
		activityDefinition.setCorrectResponsesPattern(correctResponsesPattern);
		ArrayList<InteractionComponent> choices = new ArrayList<>();
		InteractionComponent e = new InteractionComponent();
		String id = "true";
		e.setId(id);
		HashMap<String, String> desc = new HashMap<>();
		desc.put("en-US", "test example.");
		e.setDescription(desc);
		choices.add(e);
		activityDefinition.setChoices(choices);
		ArrayList<InteractionComponent> scale = new ArrayList<>();
		scale.add(e);
		activityDefinition.setScale(scale);
		ArrayList<InteractionComponent> source = new ArrayList<>();
		source.add(e);
		activityDefinition.setSource(source);
		ArrayList<InteractionComponent> target = new ArrayList<>();
		target.add(e);
		activityDefinition.setTarget(target);
		ArrayList<InteractionComponent> steps = new ArrayList<>();
		steps.add(e);
		activityDefinition.setSteps(steps);
		a.setDefinition(activityDefinition);

		// Context
		ContextActivities ca = new ContextActivities();
		ArrayList<Activity> category = new ArrayList<>();
		Activity act = new Activity("http://example.com/" + UUID.randomUUID().toString());
		category.add(act);
		ca.setCategory(category);
		Context c = new Context();
		c.setContextActivities(ca);
		statement.setContext(c);

		// Attachment
		String att = "This is a text/plain test.";
		String contentType = "text/plain";
		Attachment attachment = new Attachment();
		attachment.addAttachment(att, contentType);
		ArrayList<Attachment> attachments = new ArrayList<>();
		URI expected_type = new URI("https://mindmath.lip6.fr");
		attachment.setUsageType(expected_type);
		key = "en-US";
		HashMap<String, String> expected_display = new HashMap<>();
		expected_display.put(key, "JSON file from Cabri.");
		attachment.setDisplay(expected_display);
		attachments.add(attachment);
		att = "../mindmath2/src/main/resources/static/videos/ResolutionEquation.mp4";
		contentType = "video/mp4";
		attachment = new Attachment();
		attachment.addAttachment(att, contentType);
		attachments.add(attachment);
		statement.setAttachments(attachments);

		Gson gson = new Gson();
		String json = gson.toJson(statement);
		return new ResponseEntity<>(json, HttpStatus.ACCEPTED);
	}

	/**
	 * test to get statement json using jxapi
	 *
	 * @param data json from Cabri
	 * @return json of statement
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws JsonParserCustomException
	 * @throws URISyntaxException
	 */
	@PostMapping("/test/JXAPIexample")
	public ResponseEntity<String> testJXAPIexample(@RequestBody String data)
			throws IOException, NoSuchAlgorithmException, JsonParserCustomException, URISyntaxException {
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, "v1.0");
		Feedbackjson fbjson = new Feedbackjson(task.getSensors().getId_learner());
		XAPIgenerator generator = new XAPIgenerator();

		statement = generator.setAttachment().setResult(true, true, fbjson).generateStatement(task);

		Gson gson = new Gson();
		return new ResponseEntity<>(gson.toJson(statement), HttpStatus.ACCEPTED);
	}

	/**
	 * test to post statement json using jxapi
	 *
	 * @param data json from Cabri
	 * @return id of statement in Learning Locker
	 * @throws JsonParserCustomException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws URISyntaxException
	 */
	@PostMapping("/test/JXAPIexamplePOST")
	public ResponseEntity<String> testJXAPIexamplePOST(@RequestBody String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, "v1.0");
		Feedbackjson fbjson = new Feedbackjson(task.getSensors().getId_learner());
		XAPIgenerator generator = new XAPIgenerator();

		statement = generator.setAttachment().setResult(true, true, fbjson).generateStatement(task);

		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp();
		return new ResponseEntity<>(ll.postStatement(statement), HttpStatus.ACCEPTED);
	}
}

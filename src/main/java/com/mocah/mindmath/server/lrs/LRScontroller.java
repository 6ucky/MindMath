package com.mocah.mindmath.server.lrs;

import java.io.IOException;
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
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepository;
import com.mocah.mindmath.repository.learninglocker.XAPIgenerator;
import com.mocah.mindmath.repository.learninglocker.XAPItype;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.cabri.jsondata.Task;
import com.mocah.mindmath.repository.jxapi.Account;
import com.mocah.mindmath.repository.jxapi.Activity;
import com.mocah.mindmath.repository.jxapi.ActivityDefinition;
import com.mocah.mindmath.repository.jxapi.Agent;
import com.mocah.mindmath.repository.jxapi.Attachment;
import com.mocah.mindmath.repository.jxapi.Context;
import com.mocah.mindmath.repository.jxapi.ContextActivities;
import com.mocah.mindmath.repository.jxapi.InteractionComponent;
import com.mocah.mindmath.repository.jxapi.Statement;
import com.mocah.mindmath.repository.jxapi.StatementResult;
import com.mocah.mindmath.repository.jxapi.Verb;
import com.mocah.mindmath.repository.jxapi.Verbs;

/**
 * @author Yan Wang
 */

@RestController
@RequestMapping("/lrs")
public class LRScontroller {

	private static final String license_num = "mocah";

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
	public ResponseEntity<String> getAboutLearningLocker() {
		LearningLockerRepository ll = new LearningLockerRepository();
		StatementResult statements = ll.getAllStatementsfromLearningLocker();
		return new ResponseEntity<String>(statements.getStatements().toString(), HttpStatus.ACCEPTED);
	}

	/**
	 * Test of post for LRS
	 * 
	 * @return the added statement
	 * @throws JsonParseCustomException
	 */
	@PostMapping("/test")
	public ResponseEntity<String> testLearningLocker(@RequestBody String data,
			@RequestHeader("Authorization") String auth) throws JsonParserCustomException {
		if (checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		LearningLockerRepository ll = new LearningLockerRepository();
		JsonParserLogs parserLog = new JsonParserLogs(data);
		JsonParserSensor parserSensor = new JsonParserSensor(data);
		JsonParserFactory parserRoot = new JsonParserFactory(data);

		JsonObject response = new JsonObject();
		response.add("Response from LRS",
				JsonParser.parseString(ll.postStatementTEST(
						parserRoot.getValueAsString(parserRoot.getObject(), JsonParserKeys.TASK_ID),
						parserSensor.getSensor(), parserLog.getLogs())).getAsJsonObject());

		JsonArray statementArray = new JsonArray();
		XAPIgenerator xapi = new XAPIgenerator();
		String student_id = parserRoot.getValueAsString(parserRoot.getObject(), JsonParserKeys.TASK_ID);
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

	@PostMapping("/testJXAPI")
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
		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		URI expected_type = new URI("https://mindmath.lip6.fr");
		attachment.setUsageType(expected_type);
		key = "en-US";
		HashMap<String, String> expected_display = new HashMap<String, String>();
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
	@PostMapping("/testJXAPIexample")
	public ResponseEntity<String> testJXAPIexample(@RequestBody String data, @RequestHeader("Authorization") String auth) throws IOException, NoSuchAlgorithmException, JsonParserCustomException, URISyntaxException{
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, "v1.0");
		
		Statement statement = new Statement();
		
		Agent agent = new Agent();
		Account account = new Account("idLearner:" + task.getId_learner(), "https://www.tralalere.com/");
		agent.setAccount(account);
		statement.setActor(agent);
		
		Verb verb = Verbs.experienced();
		statement.setVerb(verb);
		
		Activity a = new Activity();
		a.setId("https://mindmath.lip6.fr");
		statement.setObject(a);
		
		String key = "en-US";
		String name = "Cabri";
		String description = "Json file from Cabri.";
		HashMap<String, String> nameMap = new HashMap<String, String>();
		HashMap<String, String> descriptionMap = new HashMap<String, String>();
		nameMap.put(key, name);
		descriptionMap.put(key, description);
		ActivityDefinition activityDefinition = new ActivityDefinition(nameMap, descriptionMap);
		HashMap<String, JsonElement> extensions = new HashMap<String, JsonElement>();
		JsonObject jo = new JsonObject();
		jo.addProperty("https://mindmath.lip6.fr/sensor/domain", task.getSensors().getDomain());
		jo.addProperty("https://mindmath.lip6.fr/sensor/generator", task.getSensors().getGenerator());
		jo.addProperty("https://mindmath.lip6.fr/sensor/taskFamily", task.getSensors().getTaskFamily());
		jo.addProperty("https://mindmath.lip6.fr/sensor/correctAnswer", task.getSensors().isCorrectAnswer());
		jo.addProperty("https://mindmath.lip6.fr/sensor/codeError", task.getSensors().getCodeError());
		key = "https://mindmath.lip6.fr/sensor";
		extensions.put(key, jo);
		for(int i = 0; i < task.getLog().size(); i++)
		{
			jo = new JsonObject();
			jo.addProperty("https://mindmath.lip6.fr/log" + i + "/action", task.getLog().get(i).getAction());
			jo.addProperty("https://mindmath.lip6.fr/log" + i + "/name", task.getLog().get(i).getName());
			jo.addProperty("https://mindmath.lip6.fr/log" + i + "/time", task.getLog().get(i).getTime());
			jo.addProperty("https://mindmath.lip6.fr/log" + i + "/type", task.getLog().get(i).getType());
			key = "https://mindmath.lip6.fr/log" + i;
			extensions.put(key, jo);
		}
		activityDefinition.setExtensions(extensions);
		a.setDefinition(activityDefinition);
		
//		Attachment attachment = new Attachment();
//		attachment.addAttachment(data, "text/plain");
//		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
//		URI expected_type = new URI("");
//		attachment.setUsageType(expected_type);
//		key = "en-US";
//		HashMap<String, String> expected_display = new HashMap<String, String>();
//		expected_display.put(key, "JSON file from Cabri.");
//		attachment.setDisplay(expected_display);
//		attachments.add(attachment);
//		statement.setAttachments(attachments);
		
		Gson gson = new Gson();
		LearningLockerRepository ll = new LearningLockerRepository();
		return new ResponseEntity<String>(gson.toJson(statement), HttpStatus.ACCEPTED);
//		return new ResponseEntity<String>(ll.postStatement(statement), HttpStatus.ACCEPTED);
	}
		
	@PostMapping("/testJXAPIexampleFB")
	public ResponseEntity<String> testJXAPIexampleFB(@RequestBody String data, @RequestHeader("Authorization") String auth) throws IOException, NoSuchAlgorithmException, JsonParserCustomException, URISyntaxException{
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, "v1.0");
		Feedbackjson fbjson = new Feedbackjson(task.getId_learner());
		
		Statement statement = new Statement();
		
		Agent agent = new Agent();
		Account account = new Account("Feedback:" + fbjson.getIdFb(), "https://mindmath.lip6.fr");
		agent.setAccount(account);
		statement.setActor(agent);
		
		Verb verb = Verbs.experienced();
		statement.setVerb(verb);
		
		Activity a = new Activity();
		a.setId("https://mindmath.lip6.fr");
		statement.setObject(a);
		
		String key = "en-US";
		String name = "Feedback";
		String description = "Feedback from MindMath LIP6.";
		HashMap<String, String> nameMap = new HashMap<String, String>();
		HashMap<String, String> descriptionMap = new HashMap<String, String>();
		nameMap.put(key, name);
		descriptionMap.put(key, description);
		ActivityDefinition activityDefinition = new ActivityDefinition(nameMap, descriptionMap);
		HashMap<String, JsonElement> extensions = new HashMap<String, JsonElement>();
		JsonObject jo = new JsonObject();
		jo.addProperty("https://mindmath.lip6.fr/feedback/idLearner", fbjson.getIdLearner());
		jo.addProperty("https://mindmath.lip6.fr/feedback/motivationalElementFb", fbjson.getMotivationalElementFb());
		jo.addProperty("https://mindmath.lip6.fr/feedback/contentFb", fbjson.getContentFb());
		jo.addProperty("https://mindmath.lip6.fr/feedback/glossaryFb", fbjson.getGlossaryFb());
		key = "https://mindmath.lip6.fr/feedback";
		extensions.put(key, jo);
		activityDefinition.setExtensions(extensions);
		a.setDefinition(activityDefinition);
		
//		Attachment attachment = new Attachment();
//		attachment.addAttachment("A video", "text/plain");
//		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
//		URI expected_type = new URI("https://mindmath.lip6.fr/videos/ResolutionEquation.mp4");
//		attachment.setUsageType(expected_type);
//		key = "en-US";
//		HashMap<String, String> expected_display = new HashMap<String, String>();
//		expected_display.put(key, "Feedback Video.");
//		attachment.setDisplay(expected_display);
//		attachments.add(attachment);
//		statement.setAttachments(attachments);
		
		Gson gson = new Gson();
		LearningLockerRepository ll = new LearningLockerRepository();
		return new ResponseEntity<String>(gson.toJson(statement), HttpStatus.ACCEPTED);
//		return new ResponseEntity<String>(ll.postStatement(statement), HttpStatus.ACCEPTED);
	}
}


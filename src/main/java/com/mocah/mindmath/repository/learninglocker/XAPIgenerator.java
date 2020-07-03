package com.mocah.mindmath.repository.learninglocker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.cabri.jsondata.Task;

import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.Verbs;

/**
 * Generate statement based on xAPI
 * 
 * @author Yan Wang
 * @since 20/04/2020
 */
public class XAPIgenerator {

	private JsonObject actorObject;
	private JsonObject verbObject;
	private JsonObject objectObject;
	private JsonObject contextObject;
	private Statement statement;

	public XAPIgenerator() {
		this.actorObject = new JsonObject();
		this.verbObject = new JsonObject();
		this.objectObject = new JsonObject();
		this.contextObject = new JsonObject();
		this.statement = new Statement();
	}

	public void setActor(String email, String name) {
		actorObject.addProperty("mbox", "mailto:" + email);
		actorObject.addProperty("name", name);
		actorObject.addProperty("objectType", "Agent");
	}

	public void setVerb() {
		JsonObject displayObject = new JsonObject();
		displayObject.addProperty("en-US", "experienced");
		verbObject.add("display", displayObject);
		verbObject.addProperty("id", "http://activitystrea.ms/schema/1.0/experience");
	}

	public void setObject(String name, String value) {
		JsonObject definitionObject = new JsonObject();
		JsonObject nameObject = new JsonObject();
		nameObject.addProperty("en-US", "Feedback Decision: sensor: " + name + "," + value);
		JsonObject desObject = new JsonObject();
		desObject.addProperty("en-US", "How to Make Feedback Decision: sensor has " + name + " is " + value);
		definitionObject.add("name", nameObject);
		definitionObject.add("description", desObject);
		objectObject.add("definition", definitionObject);
		objectObject.addProperty("id", "http://lrsmocah.lip6.fr/mindmath/feedback/sensors/" + name);
		objectObject.addProperty("objectType", "Activity");
	}

	public void setObject(int num, String action, String value) {
		JsonObject definitionObject = new JsonObject();
		JsonObject nameObject = new JsonObject();
		nameObject.addProperty("en-US", "Feedback Decision: log-" + num + ": " + action + "," + value);
		JsonObject desObject = new JsonObject();
		desObject.addProperty("en-US", "How to Make Feedback Decision: log-" + num + " has " + action + " " + value);
		definitionObject.add("name", nameObject);
		definitionObject.add("description", desObject);
		objectObject.add("definition", definitionObject);
		objectObject.addProperty("id", "http://lrsmocah.lip6.fr/mindmath/feedback/logs/" + num);
		objectObject.addProperty("objectType", "Activity");
	}

	public void setContext(XAPItype type) {
		String firstparent = "{\r\n" + "                    \"definition\": {\r\n"
				+ "                        \"name\": {\r\n"
				+ "                            \"en-US\": \"Feedback Decision\"\r\n" + "                        },\r\n"
				+ "                        \"description\": {\r\n"
				+ "                            \"en-US\": \"How to Make Feedback Decision\"\r\n"
				+ "                        }\r\n" + "                    },\r\n"
				+ "                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback\",\r\n"
				+ "                    \"objectType\": \"Activity\"\r\n" + "                }";
		String secondparent = null;
		switch (type) {
		case SENSORS:
			secondparent = "{\r\n" + "                    \"definition\": {\r\n"
					+ "                        \"name\": {\r\n"
					+ "                            \"en-US\": \"Feedback Decision: sensors\"\r\n"
					+ "                        }\r\n" + "                    },\r\n"
					+ "                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback/sensors\",\r\n"
					+ "                    \"objectType\": \"Activity\"\r\n" + "                }";
			break;
		case LOGS:
			secondparent = "{\r\n" + "                    \"definition\": {\r\n"
					+ "                        \"name\": {\r\n"
					+ "                            \"en-US\": \"Feedback Decision: logs\"\r\n"
					+ "                        }\r\n" + "                    },\r\n"
					+ "                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback/logs\",\r\n"
					+ "                    \"objectType\": \"Activity\"\r\n" + "                }";
		}
		String group = "{\r\n" + "                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/\",\r\n"
				+ "                    \"objectType\": \"Activity\"\r\n" + "                }";
		JsonArray parentArray = new JsonArray();
		parentArray.add(JsonParser.parseString(firstparent).getAsJsonObject());
		parentArray.add(JsonParser.parseString(secondparent).getAsJsonObject());
		JsonObject temp = new JsonObject();
		temp.add("parent", parentArray);
		JsonArray groupArray = new JsonArray();
		groupArray.add(JsonParser.parseString(group).getAsJsonObject());
		temp.add("grouping", groupArray);
		contextObject.add("contextActivities", temp);
	}

	public JsonObject generateStatement() {
		JsonObject rootObject = new JsonObject();
		rootObject.add("actor", actorObject);
		rootObject.add("verb", verbObject);
		rootObject.add("object", objectObject);
		rootObject.add("context", contextObject);
		return rootObject;
	}

	/**
	 * set attachment
	 * 
	 * @TODO need to give input for each statement
	 * @return this generator
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public XAPIgenerator setAttachment() throws NoSuchAlgorithmException, IOException, URISyntaxException {
		Attachment attachment = new Attachment();
		attachment.addAttachment("../mindmath2/src/main/resources/static/videos/ResolutionEquation.mp4",
				"application/octet-stream");
		ArrayList<Attachment> attachments = new ArrayList<>();
		URI expected_type = new URI("http://lrsmocah.lip6.fr/attachments/video");
		attachment.setUsageType(expected_type);
		String key = "en-US";
		HashMap<String, String> expected_display = new HashMap<>();
		expected_display.put(key, "Feedback Video.");
		attachment.setDisplay(expected_display);
		attachment.setFileUrl(new URI("https://mindmath.lip6.fr/videos/ResolutionEquation.mp4"));
		attachments.add(attachment);
		statement.setAttachments(attachments);
		return this;
	}

	/**
	 * generate statement based on jxapi, set actor, verb, object, context
	 * 
	 * @param task
	 * @param fbjson
	 * @return statement for xAPI
	 */
	public Statement generateStatement(Task task, Feedbackjson fbjson) {

		statement.setActor(task.getLearnerAsActor());

		Verb verb = Verbs.experienced();
		if (task.getVerb().equals("bouton-valider")) {
			verb = Verbs.answered();
		} else if (task.getVerb().equals("bouton-aide")) {
			verb = Verbs.asked();
		}
		statement.setVerb(verb);

		Activity a = new Activity();
		String ac_id = "https://mindmath.lip6.fr/" + fbjson.getIdFamilytask();
		a.setId(ac_id);
		String key = "fr-FR";
		String name = "resoudreEquationPremierDegre";
		String description = "algebre";
		HashMap<String, String> nameMap = new HashMap<>();
		HashMap<String, String> descriptionMap = new HashMap<>();
		nameMap.put(key, name);
		descriptionMap.put(key, description);
		ActivityDefinition activityDefinition = new ActivityDefinition(nameMap, descriptionMap);
		a.setDefinition(activityDefinition);
		statement.setObject(a);

		Result fdresult = new Result();
		boolean correctness = Boolean.getBoolean(task.getSensors().isCorrectAnswer());
		fdresult.setSuccess(correctness);
		JsonObject jo = new JsonObject();
		jo.addProperty("idFeedback", fbjson.getIdFeedback());
		jo.addProperty("motivationalElementFb", fbjson.getMotivationalElementFb());
		jo.addProperty("contentFb", fbjson.getContentFb());
		jo.addProperty("glossaryFb", fbjson.getGlossaryFb());
		JsonObject root_jo = new JsonObject();
		root_jo.add("https://mindmath.lip6.fr/feedback", jo);
		fdresult.setExtensions(root_jo);
		statement.setResult(fdresult);

		HashMap<String, JsonElement> extensions = new HashMap<>();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		extensions.put("https://mindmath.lip6.fr/sensors", gson.toJsonTree(task.getSensors()));
		extensions.put("https://mindmath.lip6.fr/logs", gson.toJsonTree(task.getLog()));
		Context c = new Context();
		c.setExtensions(extensions);
		statement.setContext(c);

		return statement;
	}

}

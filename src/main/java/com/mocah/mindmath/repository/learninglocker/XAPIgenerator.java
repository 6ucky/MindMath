package com.mocah.mindmath.repository.learninglocker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Generate statement based on xAPI
 * @author	Yan Wang
 * @since	20/04/2020
 */
public class XAPIgenerator {
	
	private JsonObject actorObject;
	private JsonObject verbObject;
	private JsonObject objectObject;
	private JsonObject contextObject;
	
	public XAPIgenerator() {
		this.actorObject = new JsonObject();
		this.verbObject = new JsonObject();
		this.objectObject = new JsonObject();
		this.contextObject = new JsonObject();
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
		nameObject.addProperty("en-US", "Feedback Decision: log-"+ num +": " + action + "," + value);
		JsonObject desObject = new JsonObject();
		desObject.addProperty("en-US", "How to Make Feedback Decision: log-" + num +" has " + action + " " + value);
		definitionObject.add("name", nameObject);
		definitionObject.add("description", desObject);
		objectObject.add("definition", definitionObject);
		objectObject.addProperty("id", "http://lrsmocah.lip6.fr/mindmath/feedback/logs/" + num);
		objectObject.addProperty("objectType", "Activity");
	}
	
	public void setContext(XAPItype type) {
		String firstparent = "{\r\n" + 
				"                    \"definition\": {\r\n" + 
				"                        \"name\": {\r\n" + 
				"                            \"en-US\": \"Feedback Decision\"\r\n" + 
				"                        },\r\n" + 
				"                        \"description\": {\r\n" + 
				"                            \"en-US\": \"How to Make Feedback Decision\"\r\n" + 
				"                        }\r\n" + 
				"                    },\r\n" + 
				"                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback\",\r\n" + 
				"                    \"objectType\": \"Activity\"\r\n" + 
				"                }";
		String secondparent = null;
		switch(type) {
		case SENSORS:
			secondparent = "{\r\n" + 
					"                    \"definition\": {\r\n" + 
					"                        \"name\": {\r\n" + 
					"                            \"en-US\": \"Feedback Decision: sensors\"\r\n" + 
					"                        }\r\n" + 
					"                    },\r\n" + 
					"                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback/sensors\",\r\n" + 
					"                    \"objectType\": \"Activity\"\r\n" + 
					"                }";
			break;
		case LOGS:
			secondparent = "{\r\n" + 
					"                    \"definition\": {\r\n" + 
					"                        \"name\": {\r\n" + 
					"                            \"en-US\": \"Feedback Decision: logs\"\r\n" + 
					"                        }\r\n" + 
					"                    },\r\n" + 
					"                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/feedback/logs\",\r\n" + 
					"                    \"objectType\": \"Activity\"\r\n" + 
					"                }";
		}
		String group = "{\r\n" + 
				"                    \"id\": \"http://lrsmocah.lip6.fr/mindmath/\",\r\n" + 
				"                    \"objectType\": \"Activity\"\r\n" + 
				"                }";
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
	
	// TODO get time from Cabri json file
	public void generateTimestamp() {
		
	}
	
}

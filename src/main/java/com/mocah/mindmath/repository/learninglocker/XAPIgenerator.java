package com.mocah.mindmath.repository.learninglocker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.jsonparser.CabriVersion;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.entity.task.Task;

import gov.adlnet.xapi.model.Activity;
import gov.adlnet.xapi.model.ActivityDefinition;
import gov.adlnet.xapi.model.Attachment;
import gov.adlnet.xapi.model.Context;
import gov.adlnet.xapi.model.ContextActivities;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.InteractionComponent;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementReference;
import gov.adlnet.xapi.model.SubStatement;
import gov.adlnet.xapi.model.Verb;

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
		attachment.addAttachment("../mindmath/src/main/resources/static/videos/ResolutionEquation.mp4",
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
	 * set result for statement
	 * 
	 * @param success    true if we call decision process, false if we decide the
	 *                   cabri JSON is gaming with the system
	 * @param completion true if decision process work, false if decision produces
	 *                   an error
	 * @param fbjson     feedback object
	 * @return this generator
	 */
	public XAPIgenerator setResult(boolean success, boolean completion, Feedbackjson fbjson, List<Motivation> motivations, CabriVersion version) {
		switch(version)
		{
		case v1_0:
			Result fdresult = new Result();
			if (success == true && completion == true) {
				JsonObject jo = new JsonObject();
				jo.addProperty("idFeedback", fbjson.getIdFeedback());
				jo.addProperty("motivationalElementFb", fbjson.getMotivationalElementFb());
				jo.addProperty("contentFb", fbjson.getContentFb());
				jo.addProperty("glossaryFb", fbjson.getGlossaryFb());
				JsonObject root_jo = new JsonObject();
				root_jo.add("https://mindmath.lip6.fr/feedback", jo);
				fdresult.setExtensions(root_jo);
			}
			fdresult.setSuccess(success);
			fdresult.setCompletion(completion);

			statement.setResult(fdresult);
		case test:
			fdresult = new Result();
			fdresult.setSuccess(success);
			fdresult.setCompletion(completion);
			fdresult.setResponse(motivations.get(new Random().nextInt(motivations.size())).getMotivation_data());
			statement.setResult(fdresult);
		}
		
		return this;
	}
	
	public XAPIgenerator setVerb(Task task) {
		Verb verb = task.getVerb();
		setVerb(verb);
		return this;
	}
	
	public XAPIgenerator setVerb(Verb verb) {
		statement.setVerb(verb);
		return this;
	}
	
	public XAPIgenerator setObject(Task task) {
		Activity a = new Activity();
		String ac_id = "https://mindmath.lip6.fr/" + task.getSensors().getTaskFamily();
		a.setId(ac_id);
		String key = "fr-FR";
		ArrayList<InteractionComponent> source = new ArrayList<InteractionComponent>();
		ArrayList<InteractionComponent> target = new ArrayList<InteractionComponent>();
		InteractionComponent ic = new InteractionComponent();
		HashMap<String, String> descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, "Example: algebre ou geometrie.");
		ic.setId("domain");
		ic.setDescription(descriptionMap);
		source.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, task.getSensors().getDomain());
		ic.setId("1");
		ic.setDescription(descriptionMap);
		target.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, "Example: resoudreEquationPremierDegre.");
		ic.setId("generator");
		ic.setDescription(descriptionMap);
		source.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, task.getSensors().getGenerator());
		ic.setId("2");
		ic.setDescription(descriptionMap);
		target.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, "ReponseJuste valeurs: true ou false.");
		ic.setId("correctAnswer");
		ic.setDescription(descriptionMap);
		source.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, task.getSensors().isCorrectAnswer());
		ic.setId("3");
		ic.setDescription(descriptionMap);
		target.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, "Example: ce_err1 ; ce_err5 ; etc.");
		ic.setId("codeError");
		ic.setDescription(descriptionMap);
		source.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, task.getSensors().getCodeError());
		ic.setId("4");
		ic.setDescription(descriptionMap);
		target.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, "Example :0, 1 ou 2.");
		ic.setId("activityMode");
		ic.setDescription(descriptionMap);
		source.add(ic);
		
		ic = new InteractionComponent();
		descriptionMap = new HashMap<String, String>();
		descriptionMap.put(key, task.getSensors().getActivityMode());
		ic.setId("5");
		ic.setDescription(descriptionMap);
		target.add(ic);
		
		ActivityDefinition activityDefinition = new ActivityDefinition();
		activityDefinition.setInteractionType("matching");
		
		HashMap<String, JsonElement> extensions = new HashMap<>();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		extensions.put("https://mindmath.lip6.fr/logs", gson.toJsonTree(task.getLog()));
		
		activityDefinition.setSource(source);
		activityDefinition.setTarget(target);
		ArrayList<String> correctResponsesPattern = new ArrayList<String>();
		correctResponsesPattern.add("domain[.]1[,]generator[.]2[,]correctAnswer[.]3[,]codeError[.]4[,]activityMode[.]5");
		activityDefinition.setCorrectResponsesPattern(correctResponsesPattern);
		activityDefinition.setExtensions(extensions);
		a.setDefinition(activityDefinition);
		statement.setObject(a);
		return this;
	}
	
	/**
	 * @param in_statement
	 * @return
	 */
	public XAPIgenerator setObject(Statement in_statement) {
		SubStatementwithObjectType substatement = new SubStatementwithObjectType();
		substatement.setActor(in_statement.getActor());
		substatement.setVerb(in_statement.getVerb());
		substatement.setObject(in_statement.getObject());
		statement.setObject(substatement);
		return this;
	}
	
	public XAPIgenerator setObject(String id) {
		StatementReferencewithObjectType ref = new StatementReferencewithObjectType();
		ref.setId(id);
		statement.setObject(ref);
		return this;
	}
	
	public XAPIgenerator setContext(ArrayList<String> glossary_choices, ArrayList<Glossaire> glossaryMap, String id, CabriVersion version) {
		switch(version)
		{
		case v1_0:
		case test:
			ArrayList<Activity> category = new ArrayList<Activity>();
			Activity a = new Activity();
			String ac_id = "https://mindmath.lip6.fr/glossary";
			a.setId(ac_id);
			String key = "fr-FR";
			String description = "Which of the glossaries are presented?";
			HashMap<String, String> descriptionMap = new HashMap<>();
			descriptionMap.put(key, description);
			ActivityDefinition activityDefinition = new ActivityDefinition();
			activityDefinition.setDescription(descriptionMap);
			activityDefinition.setInteractionType("choice");
			ArrayList<InteractionComponent> choices = new ArrayList<InteractionComponent>();
			for(Glossaire glossary : glossaryMap)
			{				
				InteractionComponent ic = new InteractionComponent();
				ic.setId(glossary.getGlossaire_name());
				HashMap<String, String> ic_description = new HashMap<>();
				ic_description.put(key, glossary.getGlossaire_content());
				ic.setDescription(ic_description);
				choices.add(ic);
			}
			activityDefinition.setChoices(choices);
			ArrayList<String> correctResponsesPattern = new ArrayList<String>();
			String correctResponsesPattern_string = "";
			for(int i = 0; i < glossary_choices.size(); i++)
			{
				if(i != 0)
					correctResponsesPattern_string += "[,]";
				correctResponsesPattern_string += glossary_choices.get(i);
			}
			correctResponsesPattern.add(correctResponsesPattern_string);
			activityDefinition.setCorrectResponsesPattern(correctResponsesPattern);
			a.setDefinition(activityDefinition);
			category.add(a);
			ContextActivities ca = new ContextActivities();
			ca.setCategory(category);
			StatementReference statementRef = new StatementReference();
			statementRef.setId(id);
			Context c = new Context();
			c.setContextActivities(ca);
			c.setStatement(statementRef);
			statement.setContext(c);
		}
		return this;
	}

	/**
	 * generate statement based on jxapi, set actor, verb, object, context
	 *
	 * @param task
	 * @param fbjson
	 * @return statement for xAPI
	 */
	public Statement generateStatement(Task task, CabriVersion version) {
		switch(version)
		{
		case v1_0:
			statement.setActor(task.getLearnerAsActor());

			Verb verb = task.getVerb();
			statement.setVerb(verb);

			Activity a = new Activity();
			String ac_id = "https://mindmath.lip6.fr/" + task.getSensors().getTaskFamily();
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

			HashMap<String, JsonElement> extensions = new HashMap<>();
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			extensions.put("https://mindmath.lip6.fr/sensors", gson.toJsonTree(task.getSensors()));
			extensions.put("https://mindmath.lip6.fr/logs", gson.toJsonTree(task.getLog()));
			Context c = new Context();
			c.setExtensions(extensions);
			statement.setContext(c);
		case test:
			statement.setActor(task.getLearnerAsActor());
		}
		return statement;
	}

}

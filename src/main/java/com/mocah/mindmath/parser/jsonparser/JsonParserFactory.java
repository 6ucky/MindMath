package com.mocah.mindmath.parser.jsonparser;

import java.util.Arrays;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.server.entity.task.Task;

/**
 * @author Yan Wang
 * @since 09/03/2020
 */
public class JsonParserFactory extends JsonParserKeys implements ParserFactory<Task> {

	protected final JsonObject rootObject;

	// constructor of jsonparser
	public JsonParserFactory(String data) {
		this.rootObject = JsonParser.parseString(data).getAsJsonObject();
	}

	/**
	 *
	 * @param Json      object
	 * @param attribute name of Json object
	 * @return String value
	 * @throws JsonParserCustomException
	 */
	public String getValueAsString(JsonObject object, String key) throws JsonParserCustomException {
		try {
			return object.get(key).getAsString();
		} catch (Exception e) {
			throwjsonexception(object, key, e, "String");
		}

		return null;
	}

	/**
	 * A concern is that a String type can be parsed as a boolean for example "abc"
	 * is parsed as false.
	 *
	 * @param Json      object
	 * @param attribute name of Json object
	 * @return boolean value
	 * @throws JsonParserCustomException
	 */
	public Boolean getValueAsBoolean(JsonObject object, String key) throws JsonParserCustomException {
		try {
			return object.get(key).getAsBoolean();
		} catch (Exception e) {
			throwjsonexception(object, key, e, "Boolean");
		}

		return null;
	}

	/**
	 *
	 * @param Json      object
	 * @param attribute name of Json object
	 * @return long value
	 * @throws JsonParserCustomException
	 */
	public Long getValueAsLong(JsonObject object, String key) throws JsonParserCustomException {
		try {
			return object.get(key).getAsLong();
		} catch (Exception e) {
			throwjsonexception(object, key, e, "Long");
		}

		return null;
	}

	/**
	 * get String value for TASK object and the storage in the database
	 *
	 * @param Json object
	 * @param the  attribute name of Json object
	 * @return string value
	 */
	public String getValueforDB(JsonObject object, String key) {
		return object.has(key) ? object.get(key).getAsString() : null;
	}

	// handle json parser exceptions
	private String throwjsonexception(JsonObject object, String key, Exception e, String type)
			throws JsonParserCustomException {
		String root = null;
		root += "id:JsonParser, ";
		root += "key:" + key + ", ";
		if (!object.has(key)) {
			root += "error:NOT_FOUND IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		} else if (object.get(key).isJsonNull()) {
			root += "error:NULL IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		} else {
			root += "error:NOT_ACCEPTABLE IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		}
	}

	public JsonObject getObject() {
		return this.rootObject;
	}

	@Override
	public Task parse(String data, String version) throws JsonParserCustomException {

		String[] versionList = {"v1.0", "test"};
		
		Task tasks = new Task();
		
		if (!Arrays.asList(versionList).contains(version))
			return tasks;

		boolean isTest = false;
		if (version.equals("test"))
			isTest = true;
		
		JsonParserSensor sensorparser = new JsonParserSensor(data);
		JsonParserParams paramsparser = new JsonParserParams(data);
		JsonParserLogs logsparser = new JsonParserLogs(data);
		tasks = new Task(getValueforDB(rootObject, TASK_NAME),
				sensorparser.getSensor(), paramsparser.getParams(), logsparser.getLogs(),
				getValueforDB(rootObject, TASK_FEEDBACK_ID), isTest);

		return tasks;
	}

}

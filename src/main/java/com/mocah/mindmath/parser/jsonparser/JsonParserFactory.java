package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	09/03/2020
 */
public class JsonParserFactory extends JsonParserKeys implements ParserFactory <Task>{
	
	protected final JsonObject rootObject;
	
	//constructor of jsonparser
	public JsonParserFactory(String data) {
		this.rootObject = JsonParser.parseString(data).getAsJsonObject();
	}
	
	/**
	 * 
	 * @param Json object
	 * @param attribute name of Json object
	 * @return String value
	 * @throws JsonParseCustomException
	 */
	public String getValueAsString(JsonObject object, String key) throws JsonParseCustomException
	{
		try {
			return object.get(key).getAsString();
		}
		catch(Exception e) {
			throwjsonexception(object, key, e, "String");
		}
		return null;
	}
	
	/**
	 * A concern is that a String type can be parsed as a boolean for example "abc" is parsed as false.
	 * @param Json object
	 * @param attribute name of Json object
	 * @return boolean value
	 * @throws JsonParseCustomException
	 */
	public boolean getValueAsBoolean(JsonObject object, String key) throws JsonParseCustomException
	{
		try {
			return object.get(key).getAsBoolean();
		}
		catch(Exception e) {
			throwjsonexception(object, key, e, "Boolean");
		}
		return (Boolean) null;
	}
	
	/**
	 * 
	 * @param Json object
	 * @param attribute name of Json object
	 * @return long value
	 * @throws JsonParseCustomException
	 */
	public long getValueAsLong(JsonObject object, String key) throws JsonParseCustomException
	{
		try {
			return object.get(key).getAsLong();
		}
		catch(Exception e) {
			throwjsonexception(object, key, e, "Long");
		}
		return (Long) null;
	}
	
	/**
	 * get String value for TASK object and the storage in the database
	 * @param Json object
	 * @param the attribute name of Json object
	 * @return string value
	 */
	protected String getValueforDB(JsonObject object, String key)
	{
		return object.has(key) ? object.get(key).getAsString() : null;
	}
	
	// handle json parser exceptions
	private String throwjsonexception(JsonObject object, String key, Exception e, String type) throws JsonParseCustomException {
		if(!object.has(key))
			throw new JsonParseCustomException(key, " does not exist.", e);
		else if(object.get(key).isJsonNull())
			throw new JsonParseCustomException(key, " is null.", e);
		else
			throw new JsonParseCustomException(key, " cannot be parsed as " + type + ".", e);
	}
	
	public JsonObject getObject() {
		return this.rootObject;
	}

	@Override
	public Task parse(String data) throws JsonParseCustomException {
		
		Task tasks = new Task();
		if(rootObject.has(TASK_ID))
		{
			JsonParserSensor sensorparser = new JsonParserSensor(data);
			JsonParserParams paramsparser = new JsonParserParams(data);
			JsonParserLogs logsparser = new JsonParserLogs(data);
			tasks = new Task(getValueforDB(rootObject,TASK_ID), 
					getValueforDB(rootObject,TASK_NAME),
					getValueforDB(rootObject,TASK_TRIGGER),
					sensorparser.getSensor(), 
					paramsparser.getParams(), 
					logsparser.getLogs()
			);
		}
		
		return tasks;
	}
	
}

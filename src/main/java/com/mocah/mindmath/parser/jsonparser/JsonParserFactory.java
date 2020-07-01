package com.mocah.mindmath.parser.jsonparser;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
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
	 * @throws JsonParserCustomException
	 */
	public String getValueAsString(JsonObject object, String key) throws JsonParserCustomException
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
	 * @throws JsonParserCustomException
	 */
	public boolean getValueAsBoolean(JsonObject object, String key) throws JsonParserCustomException
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
	 * @throws JsonParserCustomException
	 */
	public long getValueAsLong(JsonObject object, String key) throws JsonParserCustomException
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
	private String throwjsonexception(JsonObject object, String key, Exception e, String type) throws JsonParserCustomException {
		String root = null;
		root +="id:JsonParser, ";
		root +="key:" + key + ", ";
		if(!object.has(key))
		{
			root += "error:NOT_FOUND IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		}
		else if(object.get(key).isJsonNull())
		{
			root += "error:NULL IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		}
		else
		{
			root += "error:NOT_ACCEPTABLE IN TYPE OF " + type;
			throw new JsonParserCustomException(root, e);
		}
	}
	
	public JsonObject getObject() {
		return this.rootObject;
	}

	@Override
	public Task parse(String data, String version) throws JsonParserCustomException {
		
		Task tasks = new Task();
		
		//there is only one version currently
		if(!version.equals("v1.0"))
			return tasks;
		
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
					logsparser.getLogs(),
					getValueforDB(rootObject,TASK_FEEDBACK_ID)
			);
		}
		
		return tasks;
	}
	
}

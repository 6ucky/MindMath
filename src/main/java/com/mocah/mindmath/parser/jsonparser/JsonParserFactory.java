package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Params;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;
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
	
	public String getValueAsString(JsonObject object, String key)
	{
		return object.has(key) ? object.get(key).getAsString() : null;
	}
	
	public boolean getValueAsBoolean(JsonObject object, String key)
	{
		return object.has(key) ? object.get(key).getAsBoolean() : null;
	}
	
	public long getValueAsLong(JsonObject object, String key)
	{
		return object.has(key) ? object.get(key).getAsLong() : null;
	}
	
	public JsonObject getObject() {
		return this.rootObject;
	}

	@Override
	public Task parse(String data) throws JsonParserException {
		
		Task tasks = new Task();
		if(rootObject.has(getTASK_ID()))
		{
			JsonParserSensor sensorparser = new JsonParserSensor(data);
			JsonParserParams paramsparser = new JsonParserParams(data);
			JsonParserLogs logsparser = new JsonParserLogs(data);
			tasks = new Task(getValueAsString(rootObject,getTASK_ID()), 
					getValueAsString(rootObject,TASK_NAME),
					getValueAsString(rootObject,TASK_TRIGGER),
					sensorparser.getSensor(), 
					paramsparser.getParams(), 
					logsparser.getLogs()
			);
		}
		
		return tasks;
	}
	
}

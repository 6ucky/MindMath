package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mocah.mindmath.server.cabri.jsondata.Log;

/**
 * @author	Yan Wang
 * @since	10/04/2020
 */

public class JsonParserLogs extends JsonParserFactory{

	private final JsonArray logsObject;
	
	public JsonParserLogs(String data) {
		super(data);
		JsonArray emptyarray = new JsonArray();
		this.logsObject = rootObject.has(LOG) ? rootObject.get(LOG).getAsJsonArray() : emptyarray;
	}

	public List<Log> getLogs()
	{
		List<Log> logs = new ArrayList<Log>();
		for(int i = 0; i < logsObject.size(); i++)
		{
			JsonObject tempObject = logsObject.get(i).getAsJsonObject();
			Log temp = new Log(getTaskId() + "-" + i, 
					tempObject.has(LOG_TIME) ? tempObject.get(LOG_TIME).getAsLong() : -1,
					tempObject.has(LOG_TYPE) ? tempObject.get(LOG_TYPE).getAsString() : null,
					tempObject.has(LOG_NAME) ? tempObject.get(LOG_NAME).getAsString() : null,
					tempObject.has(LOG_ACTION) ? tempObject.get(LOG_ACTION).getAsString() : null);
			logs.add(temp);
		}
		
		return logs;
	}
}

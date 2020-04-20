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
	
	public JsonArray getArray() {
		return this.logsObject;
	}

	public List<Log> getLogs()
	{
		List<Log> logs = new ArrayList<Log>();
		for(int i = 0; i < logsObject.size(); i++)
		{
			JsonObject tempObject = logsObject.get(i).getAsJsonObject();
			Log temp = new Log(getValueforDB(super.getObject(),TASK_ID) + "-" + i, 
					getValueforDB(tempObject,LOG_TIME),
					getValueforDB(tempObject,LOG_TYPE),
					getValueforDB(tempObject,LOG_NAME),
					getValueforDB(tempObject,LOG_ACTION));
			logs.add(temp);
		}
		
		return logs;
	}
}

package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mocah.mindmath.server.entity.task.Log;

/**
 * @author Yan Wang
 * @since 10/04/2020
 */

public class JsonParserLogs extends JsonParserFactory {
	private final JsonArray logsObject;

	public JsonParserLogs(String data) {
		super(data);
		JsonArray emptyarray = new JsonArray();
		this.logsObject = rootObject.has(JsonParserKeys.LOG) ? rootObject.get(JsonParserKeys.LOG).getAsJsonArray()
				: emptyarray;
	}

	public JsonArray getArray() {
		return this.logsObject;
	}

	public List<Log> getLogs() {
		List<Log> logs = new ArrayList<>();
		for (int i = 0; i < logsObject.size(); i++) {
			JsonObject tempObject = logsObject.get(i).getAsJsonObject();
			Log temp = new Log(getValueforDB(tempObject, JsonParserKeys.LOG_TIME),
					getValueforDB(tempObject, JsonParserKeys.LOG_TYPE),
					getValueforDB(tempObject, JsonParserKeys.LOG_NAME),
					getValueforDB(tempObject, JsonParserKeys.LOG_ACTION));
			logs.add(temp);
		}

		return logs;
	}
}

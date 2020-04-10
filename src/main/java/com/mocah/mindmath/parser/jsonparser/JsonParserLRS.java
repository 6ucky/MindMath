package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonParserLRS extends JsonParserFactory {

	private final LRSType LRStype;
	
	public JsonParserLRS(String data, LRSType type) {
		super(data);
		this.LRStype = type;
	}
	
	public String getStatement() {
		JsonArray emptyobject = new JsonArray();
		JsonArray LRSArray = rootObject.has(LRS_STATEMENT) ? rootObject.get(LRS_STATEMENT).getAsJsonArray() : emptyobject;
		
		return LRSArray.toString();
	}

}

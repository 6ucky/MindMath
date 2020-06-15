package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonArray;

/**
 * @author	Yan Wang
 * @since	10/04/2020
 */

public class JsonParserLRS extends JsonParserFactory {

	private final LRSType LRStype;
	
	public JsonParserLRS(String data, LRSType type) {
		super(data);
		this.LRStype = type;
	}
	
	/**
	 * get statement based on GET and POST request
	 * @return the statement in String
	 */
	public String getStatement() {
		String result = null;
		
		switch(LRStype) {
		case RESPONSE:
			JsonArray emptyobject = new JsonArray();
			JsonArray LRSArray = rootObject.has(LRS_STATEMENT) ? rootObject.get(LRS_STATEMENT).getAsJsonArray() : emptyobject;
			result = LRSArray.toString();
			break;
		case POST:
			result = rootObject.toString();
			break;
		}
		
		return result;
		
	}

}

package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.cabri.jsondata.Params;

/**
 * @author	Yan Wang
 * @since	10/04/2020
 */

public class JsonParserParams extends JsonParserFactory {

	private final JsonObject paramsObject;
	
	public JsonParserParams(String data) {
		super(data);
		JsonObject emptyobject = new JsonObject();
		this.paramsObject = rootObject.has(PARAMS) ? rootObject.get(PARAMS).getAsJsonObject() : emptyobject;
	}
	
	@Override
	public JsonObject getObject() {
		return this.paramsObject;
	}

	public Params getParams() {
		Params paramsClass = new Params();
		if(rootObject.has(PARAMS))
		{
			paramsClass = new Params(
					getValueforDB(paramsObject,PARAMS_VT_2_1),
					getValueforDB(paramsObject,PARAMS_VT_2_2),
					getValueforDB(paramsObject,PARAMS_VT_2_3),
					getValueforDB(paramsObject,PARAMS_VT_2_4)
			);
		}
		return paramsClass;
	}
}

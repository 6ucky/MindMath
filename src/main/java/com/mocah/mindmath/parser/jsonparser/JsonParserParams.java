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

	public Params getParams() {
		Params paramsClass = new Params();
		if(rootObject.has(PARAMS))
		{
			JsonObject params = rootObject.get(PARAMS).getAsJsonObject();
			paramsClass = new Params(getTaskId(), 
				params.has(PARAMS_VT_2_1) ? params.get(PARAMS_VT_2_1).getAsLong() : -1, 
				params.has(PARAMS_VT_2_2) ? params.get(PARAMS_VT_2_2).getAsLong() : -1, 
				params.has(PARAMS_VT_2_3) ? params.get(PARAMS_VT_2_3).getAsBoolean() : false, 
				params.has(PARAMS_VT_2_4) ? params.get(PARAMS_VT_2_4).getAsBoolean() : false
			);
		}
		return paramsClass;
	}
	
	public long getVT_2_1()
	{
		return paramsObject.has(PARAMS_VT_2_1) ? paramsObject.get(PARAMS_VT_2_1).getAsLong() : -1;
	}
	
	public long getVT_2_2()
	{
		return paramsObject.has(PARAMS_VT_2_2) ? paramsObject.get(PARAMS_VT_2_2).getAsLong() : -1;
	}
	
	public boolean getVT_2_3()
	{
		return paramsObject.has(PARAMS_VT_2_3) ? paramsObject.get(PARAMS_VT_2_3).getAsBoolean() : false;
	}
	
	public boolean getVT_2_4()
	{
		return paramsObject.has(PARAMS_VT_2_4) ? paramsObject.get(PARAMS_VT_2_4).getAsBoolean() : false;
	}
}

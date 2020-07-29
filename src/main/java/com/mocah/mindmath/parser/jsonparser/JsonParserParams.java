package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.entity.task.Params;

/**
 * @author Yan Wang
 * @since 10/04/2020
 */

public class JsonParserParams extends JsonParserFactory {
	private final JsonObject paramsObject;

	public JsonParserParams(String data) {
		super(data);
		JsonObject emptyobject = new JsonObject();
		this.paramsObject = rootObject.has(JsonParserKeys.PARAMS)
				? rootObject.get(JsonParserKeys.PARAMS).getAsJsonObject()
				: emptyobject;
	}

	@Override
	public JsonObject getObject() {
		return this.paramsObject;
	}

	public Params getParams() {
		Params paramsClass = new Params();
		if (rootObject.has(JsonParserKeys.PARAMS)) {
			paramsClass = new Params(getValueforDB(paramsObject, JsonParserKeys.PARAMS_VT_2_1),
					getValueforDB(paramsObject, JsonParserKeys.PARAMS_VT_2_2),
					getValueforDB(paramsObject, JsonParserKeys.PARAMS_VT_2_3),
					getValueforDB(paramsObject, JsonParserKeys.PARAMS_VT_2_4));
		}

		return paramsClass;
	}
}

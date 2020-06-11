package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;

public class JsonParserCustomException extends Exception  {

	private static final long serialVersionUID = -7424548472338947774L;

	public JsonParserCustomException(JsonObject object, Throwable err) {
		super(object.toString(), err);
	}
}

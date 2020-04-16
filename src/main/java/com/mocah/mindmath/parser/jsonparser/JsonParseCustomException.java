package com.mocah.mindmath.parser.jsonparser;

public class JsonParseCustomException extends Exception  {

	private static final long serialVersionUID = -7424548472338947774L;

	public JsonParseCustomException(String key, String errorMessage, Throwable err) {
		super("JsonParser Error:" + "'" + key + "'" + errorMessage, err);
	}
}

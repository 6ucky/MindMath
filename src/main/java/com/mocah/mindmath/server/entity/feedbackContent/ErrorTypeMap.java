package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;

public class ErrorTypeMap {

	/**
	 * TODO this needs to be validate with Cabri and Tralalere
	 */
	private static Map<String, String> errorTypeMap;
	
	public static void init() {
		errorTypeMap = ImmutableMap.of(
				"ce_confusionRationnel", "1",
				"ce_inversionNumerateurDenominateur", "3",
				"ce_concatenation", "4",
				"ce_nonGestionOppose", "2",
				"ce_oppose", "2"
				);
	}
	
	public static String getErrorNum(String content)
	{
		return (errorTypeMap.containsKey(content)) ? errorTypeMap.get(content) : "1";
	}
	
	public static boolean containError(String content)
	{
		return (errorTypeMap.containsKey(content)) ? true : false;
	}
	
	public static void setError(String data) throws JsonParserCustomException {
		errorTypeMap = new HashMap<String, String>();
		JsonParserFactory parser = new JsonParserFactory(data);
		for(String key : parser.getObject().keySet())
		{
			errorTypeMap.put(key, parser.getValueAsString(parser.getObject(), key));
		}
	}
	
	public static String toStringGson() {
		Gson gson = new Gson();
		return gson.toJson(errorTypeMap);
	}
}

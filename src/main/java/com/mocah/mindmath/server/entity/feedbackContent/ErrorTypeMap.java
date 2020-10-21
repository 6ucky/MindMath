package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;

public class ErrorTypeMap {

	/**
	 * TODO this needs to be validate with Cabri and Tralalere
	 */
	private static Map<String, String> errorTypeMap;
	private static String default_error_code = "1";
	
	public static void init() {
		errorTypeMap = new HashMap<String, String>();
		errorTypeMap = ImmutableMap.of(
				"ce_confusionRationnel", "1",
				"ce_inversionNumerateurDenominateur", "3",
				"ce_concatenation", "4",
				"ce_nonGestionOppose", "2",
				"ce_oppose", "2"
				);
		default_error_code = "1";
	}
	
	public static String getErrorNum(String content)
	{
		return (errorTypeMap.containsKey(content)) ? errorTypeMap.get(content) : default_error_code;
	}
	
	public static boolean containError(String content)
	{
		return (errorTypeMap.containsKey(content)) ? true : false;
	}
	
	public static void setError(String data, String defaultcode) throws JsonParserCustomException {
		errorTypeMap = new HashMap<String, String>();
		JsonParserFactory parser = new JsonParserFactory(data);
		boolean checkpoint = true;
		for(String key : parser.getObject().keySet())
		{
			String num = parser.getValueAsString(parser.getObject(), key);
			errorTypeMap.put(key, num);
			if(num.equals(defaultcode))
				checkpoint = false;
		}
		if(checkpoint)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Default_error_code not right.");
		default_error_code = defaultcode;
	}
	
	public static String toStringGson() {
		Gson gson = new Gson();
		return gson.toJson(errorTypeMap) + "\ndefault_error_code: " + default_error_code;
	}
}

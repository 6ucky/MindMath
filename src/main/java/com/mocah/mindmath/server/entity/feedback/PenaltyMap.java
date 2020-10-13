package com.mocah.mindmath.server.entity.feedback;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;

public class PenaltyMap {
	
	private static Map<String, Double> penaltyMap;
	
	public static void init() {
		penaltyMap = new HashMap<String, Double>();
		penaltyMap.put("0.0.0.0", 0.0);
		penaltyMap.put("1.0.0.0", 0.05);
		penaltyMap.put("1.1.GC.0", 0.3);
		penaltyMap.put("1.1.GNC.0", 0.25);
		penaltyMap.put("2.0.0.XE", 0.15);
		penaltyMap.put("2.0.0.XFT", 0.1);
		penaltyMap.put("2.1.GNC.XE", 0.3);
		
		penaltyMap.put("1.2.IC.0", 0.35);
		penaltyMap.put("1.2.INC.0", 0.3);
		penaltyMap.put("3.0.0.XE", 0.25);
		penaltyMap.put("3.0.0.XFT", 0.2);
		
		penaltyMap.put("3.2.IC.XE", 0.5);
		penaltyMap.put("3.2.IC.XFT", 0.45);
		penaltyMap.put("3.2.INC.XE", 0.35);
		penaltyMap.put("3.2.INC.XFT", 0.4);
	}
	
	public static Double getPenalty(String content)
	{
		if(!penaltyMap.containsKey(content))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, content + " NOT FOUND IN PenaltyMap.");
		return penaltyMap.get(content);
	}
	
	public static void setPenalty(String data) throws JsonParserCustomException {
		penaltyMap = new HashMap<String, Double>();
		JsonParserFactory parser = new JsonParserFactory(data);
		for(String key : parser.getObject().keySet())
		{
			penaltyMap.put(key, parser.getValueAsDouble(parser.getObject(), key));
		}
	}
	
	public static String toStringGson() {
		Gson gson = new Gson();
		return gson.toJson(penaltyMap);
	}
}

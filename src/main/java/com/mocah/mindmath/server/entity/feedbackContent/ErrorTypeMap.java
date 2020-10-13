package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class ErrorTypeMap {

	/**
	 * TODO this needs to be validate with Cabri and Tralalere
	 */
	private static final Map<String, String> errorTypeMap = ImmutableMap.of(
			"ce_confusionRationnel", "1",
			"ce_inversionNumerateurDenominateur", "3",
			"ce_concatenation", "4",
			"ce_nonGestionOppose", "2",
			"ce_oppose", "2"
			);
	public static String getErrorNum(String content)
	{
		return (errorTypeMap.containsKey(content)) ? errorTypeMap.get(content) : "1";
	}
	
	public static boolean containError(String content)
	{
		return (errorTypeMap.containsKey(content)) ? true : false;
	}
}

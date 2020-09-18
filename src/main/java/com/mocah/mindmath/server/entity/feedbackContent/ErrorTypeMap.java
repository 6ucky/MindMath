package com.mocah.mindmath.server.entity.feedbackContent;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class ErrorTypeMap {

	/**
	 * TODO this needs to be validate with Cabri and Tralalere
	 */
	private static final Map<String, String> errorTypeMap = ImmutableMap.of(
			"ce_confusionRationnel", "1",
			"ce_inversionNumerateurDenominateur", "2",
			"ce_concatenation", "3",
			"ce_nonGestionOppose", "4",
			"ce_oppose", "4"
			);
	public static String getErrorNum(String content)
	{
		return (errorTypeMap.get(content) == null) ? "1" : errorTypeMap.get(content);
	}
}

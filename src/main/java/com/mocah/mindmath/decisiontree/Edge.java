/**
 *
 */
package com.mocah.mindmath.decisiontree;

import com.google.gson.JsonPrimitive;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Edge {
	private JsonPrimitive value;
	private String query;

	public JsonPrimitive getValue() {
		return value;
	}
}

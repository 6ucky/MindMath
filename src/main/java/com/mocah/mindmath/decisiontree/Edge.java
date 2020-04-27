/**
 *
 */
package com.mocah.mindmath.decisiontree;

import java.util.List;

import com.google.gson.JsonPrimitive;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Edge {
	private JsonPrimitive value;
	private String query;
	private List<Vars> vars;

	public JsonPrimitive getValue() {
		return value;
	}

	public String getQuery() {
		return query;
	}

	public List<Vars> getVars() {
		return vars;
	}
}

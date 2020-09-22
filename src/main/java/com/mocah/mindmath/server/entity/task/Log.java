package com.mocah.mindmath.server.entity.task;

import java.io.Serializable;

import javax.persistence.Entity;

import com.google.gson.annotations.Expose;
import com.mocah.mindmath.server.entity.AbstractJsonData;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Log extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 6036741035696456506L;

	@Expose (serialize = true) 
	private final String time;

	@Expose (serialize = true) 
	private final String type;

	@Expose (serialize = true) 
	private final String name;

	@Expose (serialize = true) 
	private final String action;

	// empty object
	public Log() {
		this(null, null, null, null);
	}

	public Log(String time, String type, String name, String action) {
		super();
		this.time = time;
		this.type = type;
		this.name = name;
		this.action = action;
	}

	public String getTime() {
		return time;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getAction() {
		return action;
	}

}

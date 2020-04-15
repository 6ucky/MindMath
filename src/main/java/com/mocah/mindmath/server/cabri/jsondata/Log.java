package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
public class Log implements Serializable{

	private static final long serialVersionUID = 6036741035696456506L;

	@Id
	private String id;
	
	private final String time;
	
	private final String type;
	
	private final String name;
	
	private final String action;
	
	// empty object
	public Log() {
		this.id = null;
		this.time = null;
		this.name = null;
		this.action = null;
		this.type = null;
	}
	
	public Log(String id) {
		this();
		this.id = id;
	}
	
	public Log(String id, String time, String type, String name, String action) {
		this.id = id;
		this.time = time;
		this.type = type;
		this.name = name;
		this.action = action;
	}

	public String getId() {
		return id;
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

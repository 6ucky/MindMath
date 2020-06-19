package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Log extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 6036741035696456506L;

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator="logs_id")  
	@SequenceGenerator(name="logs_id", sequenceName="logs", initialValue = 1, allocationSize = 1)
	private long id;

	private final String time;

	private final String type;

	private final String name;

	private final String action;

	// empty object
	public Log() {
		super();
		this.time = null;
		this.name = null;
		this.action = null;
		this.type = null;
	}

	public Log(String time, String type, String name, String action) {
		super();
		this.time = time;
		this.type = type;
		this.name = name;
		this.action = action;
	}

	public long getId() {
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

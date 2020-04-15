package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
public class Task implements Serializable{

	private static final long serialVersionUID = 4790322015762458488L;

	@Id
    private final String id;

	private final String task;

	private final String trigger;

	@OneToOne(cascade = CascadeType.ALL)
	private final Sensors sensors;

	@OneToOne(cascade = CascadeType.ALL)
	private final Params params;

	@OneToMany(cascade=CascadeType.ALL)
	private final List<Log> log;
	
	// empty object
	public Task() {
		List<Log> emptylist = new ArrayList<Log>();
		this.log = emptylist;
		this.params = new Params("");
		this.trigger = null;
		this.task = null;
		this.sensors = new Sensors("");
		this.id = "";
	}
	
	public Task(String id, String task, String trigger, Sensors sensors, Params params, List<Log> log) {
		this.id = id;
		this.task = task;
		this.trigger = trigger;
		this.sensors = sensors;
		this.params = params;
		this.log = log;
	}

	public String getId() {
		return id;
	}

	public String getTask() {
		return task;
	}

	public String getTrigger() {
		return trigger;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public Params getParams() {
		return params;
	}

	public List<Log> getLog() {
		return log;
	}

}

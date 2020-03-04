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
    private String id;

	private String task;

	private String trigger;

	@OneToOne(cascade = CascadeType.ALL)
	private Sensors sensors;

	@OneToOne(cascade = CascadeType.ALL)
	private Params params;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Log> log = new ArrayList<>();
	
	public Task() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public void setSensors(Sensors sensors) {
		this.sensors = sensors;
	}

	public Params getParams() {
		return params;
	}

	public void setParams(Params params) {
		this.params = params;
	}

	public List<Log> getLog() {
		return log;
	}

	public void setLog(List<Log> log) {
		this.log = log;
	}

}

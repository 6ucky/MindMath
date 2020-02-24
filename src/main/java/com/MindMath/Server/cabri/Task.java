package com.MindMath.Server.cabri;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
@Table(name="tasks") 
public class Task{

	@Id
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("task")
	private String task;
	
	@JsonProperty("trigger")
	private String trigger;

	@OneToOne(cascade=CascadeType.ALL)
	private Sensors sensors;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Params params;
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<Log> log = new ArrayList<>();
	
	protected Task() {}

	public String getId() {
		return id;
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

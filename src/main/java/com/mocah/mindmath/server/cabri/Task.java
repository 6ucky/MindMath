package com.mocah.mindmath.server.cabri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

@Entity
@Table(name="tasks") 
public class Task implements Serializable{

	private static final long serialVersionUID = 4790322015762458488L;

	@Id
	private String id;
	
	private String task;
	
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

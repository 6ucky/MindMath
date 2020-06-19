package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Task extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 4790322015762458488L;

	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator="task_id")  
	@SequenceGenerator(name="task_id", sequenceName="task", initialValue = 1, allocationSize = 1) 
	private long id;
	
	private final String id_learner;

	private final String task;

	private final String trigger;

	@OneToOne(cascade = CascadeType.ALL)
	private final Sensors sensors;

	@OneToOne(cascade = CascadeType.ALL)
	private final Params params;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> log;

	// empty object
	public Task() {
		super();
		List<Log> emptylist = new ArrayList<>();
		this.log = emptylist;
		this.params = new Params();
		this.trigger = null;
		this.task = null;
		this.sensors = new Sensors();
		this.id_learner = "";
	}
	
	public Task(long id) {
		this();
		this.id = id;
	}

	public Task(String id_learner, String task, String trigger, Sensors sensors, Params params, List<Log> log) {
		super();
		this.id_learner = id_learner;
		this.task = task;
		this.trigger = trigger;
		this.sensors = sensors;
		this.params = params;
		this.log = log;
	}

	public String getId_learner() {
		return id_learner;
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

	public long getId() {
		return id;
	}
}

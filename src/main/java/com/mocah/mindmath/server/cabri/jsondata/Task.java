package com.mocah.mindmath.server.cabri.jsondata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import com.mocah.mindmath.repository.jxapi.Account;
import com.mocah.mindmath.repository.jxapi.Actor;
import com.mocah.mindmath.repository.jxapi.Agent;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Task extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 4790322015762458488L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id")
	@SequenceGenerator(name = "task_id", sequenceName = "task", initialValue = 1, allocationSize = 1)
	private long id;

	private final String id_learner;

	private final String task;

	private final String trigger;

	@OneToOne(cascade = CascadeType.ALL)
	private final Sensors sensors;

	@OneToOne(cascade = CascadeType.ALL)
	private final Params params;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> logs;

	private String feedback_id;

	// empty object
	public Task() {
		super();
		List<Log> emptylist = new ArrayList<>();
		this.logs = emptylist;
		this.params = new Params();
		this.trigger = null;
		this.task = null;
		this.sensors = new Sensors();
		this.id_learner = "";
	}

	public Task(String feedback_id) {
		this();
		this.feedback_id = feedback_id;
	}

	public Task(String id_learner, String task, String trigger, Sensors sensors, Params params, List<Log> log,
			String feedback_id) {
		super();
		this.id_learner = id_learner;
		this.task = task;
		this.trigger = trigger;
		this.sensors = sensors;
		this.params = params;
		this.logs = log;
		this.feedback_id = feedback_id;
	}

	public void setFeedback(String feedback_id) {
		this.feedback_id = feedback_id;
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
		return logs;
	}

	public long getId() {
		return id;
	}

	public String getFeedback() {
		return feedback_id;
	}

	public Actor getLearnerAsActor() {
		Account account = new Account(this.getId_learner(), "?");
		Actor actor = new Agent("?", account);

		return actor;
	}

	public String getVerb() {
		ListIterator<Log> listIterator = this.logs.listIterator(this.logs.size());

		while (listIterator.hasPrevious()) {
			Log log = listIterator.previous();
			if (log.getType().equals("button")) {
				if (log.getName().equals("bouton-valider") || log.getName().equals("bouton-aide"))
					return log.getName();
			}
		}

		return null;
	}
}

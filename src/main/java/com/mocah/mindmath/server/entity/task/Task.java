package com.mocah.mindmath.server.entity.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.mocah.mindmath.server.entity.AbstractJsonData;

import gov.adlnet.xapi.model.Account;
import gov.adlnet.xapi.model.Agent;
import gov.adlnet.xapi.model.Verb;
import gov.adlnet.xapi.model.Verbs;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@Entity
public class Task extends AbstractJsonData implements Serializable {

	private static final long serialVersionUID = 4790322015762458488L;

	private final String id_learner;

	private final String task;

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
		this.task = null;
		this.sensors = new Sensors();
		this.id_learner = "";
	}

	public Task(String feedback_id) {
		this();
		this.feedback_id = feedback_id;
	}

	public Task(String id_learner, String task, Sensors sensors, Params params, List<Log> log, String feedback_id) {
		super();
		this.id_learner = id_learner;
		this.task = task;
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

	public Sensors getSensors() {
		return sensors;
	}

	public Params getParams() {
		return params;
	}

	public List<Log> getLog() {
		return logs;
	}

	public String getFeedback() {
		return feedback_id;
	}

	public Agent getLearnerAsActor() {

		Agent agent = new Agent();
		Account account = new Account(this.getId_learner(), "https://www.tralalere.com/");
		agent.setAccount(account);

		return agent;
	}

	public String getTrigger() {
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

	public Verb getVerb() {
		Verb verb = Verbs.experienced();
		switch (getTrigger()) {
		case "bouton-valider":
			verb = Verbs.answered();
			break;
		case "bouton-aide":
			verb = Verbs.asked();
			break;
		default:
			break;
		}

		return verb;
	}
}

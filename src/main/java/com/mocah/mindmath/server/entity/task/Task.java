package com.mocah.mindmath.server.entity.task;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.mocah.mindmath.learning.utils.actions.IAction;
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

	private final String task;

	@OneToOne(cascade = CascadeType.ALL)
	private final Sensors sensors;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> logs;

	private String feedback_id;

	private boolean has_decision = false;
	@Transient
	private IAction decision_action;
	@Lob
	private byte[] action_as_bytes;
	private double reward;

	private boolean isTest = false;

	// Cabri defines the file expert mode, if true, query decision tree, if false, qlearning.
	private boolean expertMode = false;
	
	// test store for LRS
	private boolean useTestLRS = false;
	
	
	private boolean verbose = false;

	// empty object
	public Task() {
		this(null, new Sensors(), new ArrayList<Log>(), null, false);
	}

	public Task(String task) {
		this(task, new Sensors(), new ArrayList<Log>(), null, false);
	}

	public Task(boolean isTest) {
		this(null, new Sensors(), new ArrayList<Log>(), null, isTest);
	}

	public Task(String task, Sensors sensors, List<Log> log, String feedback_id) {
		this(task, sensors, log, feedback_id, false);
	}

	public Task(String task, Sensors sensors, List<Log> log, String feedback_id, boolean isTest) {
		super();
		this.task = task;
		this.sensors = sensors;
		this.logs = log;
		this.feedback_id = feedback_id;
		this.isTest = isTest;
	}

	public String getTask() {
		return task;
	}

	public Sensors getSensors() {
		return sensors;
	}

	public List<Log> getLog() {
		return logs;
	}

	@Deprecated
	public void setFeedback(String feedback_id) {
		this.feedback_id = feedback_id;
	}

	public String getFeedback() {
		return feedback_id;
	}

	public void setDecisionAction(IAction action) {
		this.has_decision = (action != null) ? true : false;
		this.decision_action = action;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(this.decision_action);

			this.action_as_bytes = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.feedback_id = action.getId();
	}

	public IAction getDecisionAction() {
		if (!this.has_decision)
			return null;

		if (this.decision_action == null) {
			ByteArrayInputStream bais = new ByteArrayInputStream(this.action_as_bytes);
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(bais);
				this.decision_action = (IAction) ois.readObject();
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return this.decision_action;
	}

	public double getReward() {
		return this.reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	public Agent getLearnerAsActor() {
		Agent agent = new Agent();
		Account account = new Account("Learner:" + this.getSensors().getId_learner(), "https://www.tralalere.com/");
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
			verb = Verbs.answered();
			break;
		}

		return verb;
	}

	public boolean isTest() {
		return isTest;
	}

	/**
	 * Expert Mode : use decision tree instead of learning process
	 *
	 * @return if task use expert mode
	 */
	public boolean isExpertMode() {
		return expertMode;
	}

	/**
	 * Expert Mode : use decision tree instead of learning process
	 *
	 * @param expertMode define if task use expert mode
	 */
	public void setExpertMode(boolean expertMode) {
		this.expertMode = expertMode;
	}

	public boolean isUsingTestLRS() {
		return this.useTestLRS;
	}

	public void setUsingTestLRS(boolean useTestLRS) {
		this.useTestLRS = useTestLRS;
	}

	/**
	 * @return if task is verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param isVerbose if task is verbose
	 */
	public void setVerbose(boolean isVerbose) {
		this.verbose = isVerbose;
	}
}

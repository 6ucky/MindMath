/**
 *
 */
package com.mocah.mindmath.learning;

import java.util.ArrayList;
import java.util.List;

import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Decision {
	private IAction action;

	private boolean hasLearn;
	private IState modifiedState;
	private ArrayList<IValue> modifiedQvalues;
	private double reward;
	private String error_type;

	public Decision() {
		this.hasLearn = false;
		this.reward = 0.0;
	}

	public Decision(IAction action) {
		this();
		this.action = action;
	}
	
	public Decision(IAction action, String error_type) {
		this(action);
		this.error_type = error_type;
	}

	/**
	 * @return the action decided
	 */
	public IState getState() {
		return action.getState();
	}

	/**
	 * @return the action decided
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * @param action the action decided to define
	 */
	public void setAction(IAction action) {
		this.action = action;
	}

	/**
	 * @return if decision has learn something
	 */
	public boolean hasLearn() {
		return hasLearn;
	}

	/**
	 * Note: {@code setReward(double reward)} will call this method with true
	 * parameter
	 *
	 * @param hasLearn define if decision has learn something
	 */
	public void setHasLearn(boolean hasLearn) {
		this.hasLearn = hasLearn;
	}

	/**
	 * See {@code setModifiedQvalues()}
	 *
	 * @return the state which qvalues were modified by learning
	 */
	public IState getModifiedState() {
		return modifiedState;
	}

	/**
	 * See {@code setModifiedQvalues()}
	 *
	 * @return the modified qValues by learning
	 */
	public ArrayList<IValue> getModifiedQvalues() {
		return modifiedQvalues;
	}

	/**
	 * Generaly, might be called after setting the reward
	 *
	 * @param state   the state which qvalues were modified by learning
	 * @param qvalues the modified qValues by learning
	 */
	public void setModifiedQvalues(IState state, List<IValue> qvalues) {
		this.modifiedState = state;
		this.modifiedQvalues = new ArrayList<>(qvalues);
	}

	/**
	 * @return the reward calc
	 */
	public double getReward() {
		return reward;
	}

	/**
	 * Also call {@code setHasLearn(true)}
	 *
	 * @param reward the reward value to define
	 */
	public void setReward(double reward) {
		this.reward = reward;
		setHasLearn(true);
	}

	public String getError_type() {
		return error_type;
	}

	public void setError_type(String error_type) {
		this.error_type = error_type;
	}
}

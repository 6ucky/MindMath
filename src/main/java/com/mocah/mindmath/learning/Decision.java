/**
 *
 */
package com.mocah.mindmath.learning;

import com.mocah.mindmath.learning.utils.actions.IAction;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Decision {
	private IAction action;

	private boolean hasLearn;
	private double reward;

	public Decision() {
		this.hasLearn = false;
		this.reward = 0.0;
	}

	public Decision(IAction action) {
		this();
		this.action = action;
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
	 * @return the reward calc
	 */
	public double getReward() {
		return reward;
	}

	/**
	 * @param reward the reward value to define
	 */
	public void setReward(double reward) {
		this.reward = reward;
		setHasLearn(true);
	}
}

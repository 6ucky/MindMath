package com.mocah.mindmath.learning.utils.actions;

import java.io.Serializable;

import com.mocah.mindmath.learning.utils.states.IState;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IAction extends Serializable {
	/**
	 * Get the identifier of an action
	 *
	 * @return an id
	 */
	public String getId();

	/**
	 * Get the state associated with that action
	 *
	 * @return a state
	 */
	public IState getState();
}

package com.MindMath.learning.utils.values;

import com.MindMath.learning.utils.actions.IAction;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IValue {

	/**
	 * Get the action associated with the value
	 *
	 * @return null if no action, else the action instance
	 */
	public IAction myAction();

	/**
	 * Check if there is an action associated
	 *
	 * @return true/false
	 */
	public boolean hasAction();

	/**
	 * Get the value
	 *
	 * @return the value
	 */
	public double getValue();
}

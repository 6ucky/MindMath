package com.mocah.mindmath.learning.utils.values;

import com.mocah.mindmath.learning.utils.actions.IAction;

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

	/**
	 * Set a new value
	 *
	 * @param value the new value
	 */
	public void setValue(double value);
}

/**
 *
 */
package com.MindMath.learning.policies;

import java.util.List;

import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IPolicy {

	/**
	 * Choose an action
	 *
	 * @param values the list of values for each action
	 * @return the action choosed
	 */
	public IAction chooseAction(List<IValue> values);
}

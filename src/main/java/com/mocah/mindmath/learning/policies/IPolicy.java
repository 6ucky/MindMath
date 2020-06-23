/**
 *
 */
package com.mocah.mindmath.learning.policies;

import java.io.Serializable;
import java.util.List;

import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IPolicy extends Serializable {

	/**
	 * Choose an action
	 *
	 * @param values the list of values for each action
	 * @return the action choosed
	 */
	public IAction chooseAction(List<IValue> values);
}

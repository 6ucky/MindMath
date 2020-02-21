/**
 *
 */
package com.MindMath.learning.policies;

import java.util.List;

import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.values.IValue;

/**
 * @author tsimonfine
 *
 */
public interface IPolicy {

	/**
	 * @param values
	 * @return
	 */
	public IAction chooseAction(List<IValue> values);
}

/**
 *
 */
package com.mocah.mindmath.learning.policies;

import java.util.List;

import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Greedy implements IPolicy {
	/**
	 *
	 */
	private static final long serialVersionUID = -7467762828767906208L;

	public Greedy() {
	}

	@Override
	public IAction chooseAction(List<IValue> values) {
		int actionsCount = values.size();

		// find best action (greedy)
		IValue maxValue = values.get(0);

		for (int i = actionsCount - 1; i >= 0; i--) {
			IValue value = values.get(i);
			if (value.getValue() > maxValue.getValue()) {
				maxValue = value;
			}
		}

		return maxValue.myAction();
	}

}

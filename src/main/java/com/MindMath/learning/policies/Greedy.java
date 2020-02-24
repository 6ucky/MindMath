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
public class Greedy implements IPolicy {

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

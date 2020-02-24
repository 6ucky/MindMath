/**
 *
 */
package com.MindMath.learning.policies;

import java.util.List;
import java.util.Random;

import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class EpsilonGreedy extends Greedy implements IPolicy {
	private double epsilon;
	private Random rand;

	/**
	 * Create an epsilon greedy policy
	 *
	 * @param epsilon the epsilon
	 */
	public EpsilonGreedy(double epsilon) {
		this.epsilon = epsilon;
		this.rand = new Random();
	}

	@Override
	public IAction chooseAction(List<IValue> values) {
		int actionsCount = values.size();

		// exploit
		IAction action = super.chooseAction(values);

		if (actionsCount > 1 && rand.nextDouble() < epsilon) {
			// explore
			IAction randomAction;

			do {
				int i = rand.nextInt(actionsCount);
				randomAction = values.get(i).myAction();
			} while (randomAction.equals(action));

			action = randomAction;
		}

		return action;
	}

}

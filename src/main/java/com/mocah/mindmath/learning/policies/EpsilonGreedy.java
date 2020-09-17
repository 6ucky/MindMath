/**
 *
 */
package com.mocah.mindmath.learning.policies;

import java.util.List;
import java.util.Random;

import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.values.IValue;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class EpsilonGreedy extends Greedy {
	/**
	 *
	 */
	private static final long serialVersionUID = -2578033939328725041L;

	protected double epsilon;
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

			System.out.println("[Decision] " + this.getClass().getName() + " I'm exploring.");

			do {
				int i = rand.nextInt(actionsCount);
				randomAction = values.get(i).myAction();
			} while (!randomAction.equals(action));

			action = randomAction;
		} else {
			System.out.println("[Decision] " + this.getClass().getName() + " I'm exploiting.");
		}

		return action;
	}
}

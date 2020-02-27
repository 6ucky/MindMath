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
public class DecayingEpsilonGreedy extends EpsilonGreedy {
	protected double decayRate;
	protected DecayType operator;
	protected double maxEpsilon;
	protected double minEpsilon;

	/**
	 * @param epsilon
	 * @param decayRate
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate) {
		this(epsilon, decayRate, 0, 1, DecayType.SUBSTRACT);
	}

	/**
	 * @param epsilon
	 * @param decayRate
	 * @param operation
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate, DecayType operation) {
		this(epsilon, decayRate, 0, 1, operation);
	}

	/**
	 * @param epsilon
	 * @param decayRate
	 * @param minEpsilon
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate, double minEpsilon) {
		this(epsilon, decayRate, minEpsilon, 1, DecayType.SUBSTRACT);
	}

	/**
	 * @param epsilon
	 * @param decayRate
	 * @param minEpsilon
	 * @param operation
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate, double minEpsilon, DecayType operation) {
		this(epsilon, decayRate, minEpsilon, 1, operation);
	}

	/**
	 * @param epsilon
	 * @param decayRate
	 * @param minEpsilon
	 * @param maxEpsilon
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate, double minEpsilon, double maxEpsilon) {
		this(epsilon, decayRate, minEpsilon, maxEpsilon, DecayType.SUBSTRACT);
	}

	/**
	 * @param epsilon
	 * @param decayRate
	 * @param minEpsilon
	 * @param maxEpsilon
	 * @param operation
	 */
	public DecayingEpsilonGreedy(double epsilon, double decayRate, double minEpsilon, double maxEpsilon,
			DecayType operation) {
		super(epsilon);
		this.decayRate = decayRate;
		this.operator = operation;
		this.maxEpsilon = maxEpsilon;
		this.minEpsilon = minEpsilon;
	}

	@Override
	public IAction chooseAction(List<IValue> values) {
		IAction action = super.chooseAction(values);

		decay();

		return action;
	}

	/**
	 * Modify the epsilon value based on parameters of the policy (call after each
	 * chooseAction call)
	 */
	private void decay() {
		double newEpsilon = this.epsilon;

		switch (this.operator) {
		case ADD:
			newEpsilon += this.decayRate;
			break;

		case DIVIDE:
			newEpsilon /= this.decayRate;
			break;

		case MULTIPLY:
			newEpsilon *= this.decayRate;
			break;

		case SUBSTRACT:
		default:
			newEpsilon -= this.decayRate;
			break;
		}

		// Bound epsilon
		this.epsilon = Math.min(Math.max(newEpsilon, this.minEpsilon), this.maxEpsilon);
	}
}

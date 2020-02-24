package com.MindMath.learning.algorithms;

import java.util.List;
import java.util.Map;

import com.MindMath.learning.policies.IPolicy;
import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.states.IState;
import com.MindMath.learning.utils.values.IValue;

public class QLearning extends AbstractLearning {
	private Map<IState, List<IValue>> qValues;

	// learning rate
	private double alpha;
	// discount factor
	private double gamma;

	public QLearning() {

	}

	/**
	 * Change the policy of this Qlearning
	 *
	 * @param newPolicy The new policy
	 */
	public void setExplorationPolicy(IPolicy newPolicy) {
		this.policy = newPolicy;
	}

	/**
	 * @return the learning rate
	 */
	public double getLearningRate() {
		return alpha;
	}

	/**
	 * @param alpha new learning rate
	 */
	public void setLearningRate(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return the discount factor
	 */
	public double getDiscountFactor() {
		return gamma;
	}

	/**
	 * @param gamma new discount factor
	 */
	public void setDiscountFactor(double gamma) {
		this.gamma = gamma;
	}

	@Override
	public IAction step(IState state) {
		List<IValue> values = this.qValues.get(state);
		IAction action = this.policy.chooseAction(values);

		return action;
	}

	@Override
	public void learn(IState oldState, IAction action, double reward, IState newState) {
		// TODO Stub de la méthode généré automatiquement
		List<IValue> nextActionEstim = this.qValues.get(newState);

		// Find max expected reward for next state
		double maxExpectedRew = nextActionEstim.get(0).getValue();
		for (IValue val : nextActionEstim) {
			if (val.getValue() > maxExpectedRew) {
				maxExpectedRew = val.getValue();
			}
		}

		// Edit qvalue
		List<IValue> prevActionEstim = this.qValues.get(oldState);
		for (IValue iValue : prevActionEstim) {
			if (iValue.myAction().equals(action)) {
				double v = iValue.getValue();
				v *= (1 - this.alpha);
				v += (this.alpha * (reward + this.gamma * maxExpectedRew));

				iValue.setValue(v);
			}
		}

	}

}

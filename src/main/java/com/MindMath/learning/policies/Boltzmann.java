/**
 *
 */
package com.MindMath.learning.policies;

import java.util.List;
import java.util.Random;

import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.values.IValue;

/**
 * Softmax
 *
 * @author tsimonfine
 *
 */
public class Boltzmann implements IPolicy {
	protected double temperature;
	protected Random rand;

	/**
	 * @param temperature
	 */
	public Boltzmann(double temperature) {
		this.temperature = temperature;
		this.rand = new Random();
	}

	/**
	 * @return temperature
	 */
	public double getTemperature() {
		return temperature;
	}

	/**
	 * @param temperature temperature à définir
	 */
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	@Override
	public IAction chooseAction(List<IValue> values) {
		int actionsCount = values.size();
		double[] actionSoftMax = softMax(values);

		// System.out.println(Arrays.toString(actionSoftMax));

		// Begin discrete prob
		double d = rand.nextDouble();

		double[] cumprob = new double[actionSoftMax.length + 1];
		cumprob[0] = 0;
		double total = 0;
		for (int i = 0; i < actionSoftMax.length; i++) {
			total += actionSoftMax[i];
			cumprob[i + 1] = total;
		}

		IAction action = null;

		for (int i = 0; i < actionSoftMax.length; i++) {
			if (d > cumprob[i] && d <= cumprob[i + 1]) {
				action = values.get(i).myAction();
				break;
			}
		}
		// End discrete prob

		if (action == null) {
			int i = rand.nextInt(actionsCount);
			action = values.get(i).myAction();
		}

		return action;
	}

	private double[] softMax(List<IValue> values) {
		int actionsCount = values.size();

		double[] actionSoftMax = new double[actionsCount];
		double sum = 0;

		for (int i = 0; i < actionsCount; ++i) {
			actionSoftMax[i] = Math.exp(temperature * values.get(i).getValue());
			sum += actionSoftMax[i];
		}

		for (int i = 0; i < actionSoftMax.length; ++i) {
			actionSoftMax[i] /= sum;
		}

		return actionSoftMax;
	}
}

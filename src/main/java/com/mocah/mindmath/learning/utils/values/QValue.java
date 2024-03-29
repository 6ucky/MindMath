/**
 *
 */
package com.mocah.mindmath.learning.utils.values;

import com.mocah.mindmath.learning.utils.actions.IAction;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class QValue implements IValue {
	/**
	 *
	 */
	private static final long serialVersionUID = 2706852718980606287L;

	private IAction action;
	private double value;

	/**
	 * Instantiate a QValue with no action associated and value set to 0
	 */
	public QValue() {
		this(null, 0);
	}

	/**
	 * QValue with action associated and value set to 0
	 *
	 * @param a an IAction associated with this Qvalue
	 */
	public QValue(IAction a) {
		this(a, 0);
	}

	/**
	 * QValue with action associated and value set to 0
	 *
	 * @param a
	 * @param defaultValue
	 */
	public QValue(IAction a, double defaultValue) {
		this.action = a;
		this.value = defaultValue;
	}

	@Override
	public IAction myAction() {
		return this.action;
	}

	@Override
	public boolean hasAction() {
		return this.action != null;
	}

	@Override
	public double getValue() {
		return this.value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return action + "→" + value + ";";
	}
}

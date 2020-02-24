/**
 *
 */
package com.MindMath.learning.utils.values;

import com.MindMath.learning.utils.actions.IAction;

/**
 * @author tsimonfine
 *
 */
public class QValue implements IValue {
	private IAction action;
	private double value;

	/**
	 *
	 */
	public QValue() {
		this(null, 0);
	}

	/**
	 * @param a
	 */
	public QValue(IAction a) {
		this(a, 0);
	}

	/**
	 * @param a
	 * @param defaultValue
	 */
	public QValue(IAction a, double defaultValue) {
		this.action = a;
		this.value = defaultValue;
	}

	@Override
	public IAction myAction() {
		return this.myAction();
	}

	@Override
	public boolean hasAction() {
		return this.action != null;
	}

	@Override
	public double getValue() {
		return this.value;
	}
}

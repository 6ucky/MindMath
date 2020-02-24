package com.MindMath.learning.utils.values;

import com.MindMath.learning.utils.actions.IAction;

public interface IValue {

	public IAction myAction();

	public boolean hasAction();

	public double getValue();
}

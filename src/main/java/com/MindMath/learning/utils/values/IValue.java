package com.MindMath.learning.utils.values;

import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.states.IState;

public interface IValue {

	public IState myState();

	public IAction myAction();

	public double getValue();

}

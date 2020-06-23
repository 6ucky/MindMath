/**
 *
 */
package com.mocah.mindmath.learning.algorithms;

import java.io.Serializable;

import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface ILearning extends Serializable {

	/**
	 * Make a choice for the next action
	 *
	 * @param state
	 * @return the action choosed or to do
	 */
	public IAction step(IState state);

	/**
	 * Learn from a previous step
	 *
	 * @param oldState the state of the environment before the action was done
	 * @param action   the action done
	 * @param reward   the reward obtained
	 * @param newState the state of the environment after the action was done
	 */
	public void learn(IState oldState, IAction action, double reward, IState newState);
}

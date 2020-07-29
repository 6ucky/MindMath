/**
 *
 */
package com.mocah.mindmath.learning.algorithms;

import java.io.Serializable;
import java.util.List;

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
	 * @param state the current state
	 * @return the chosen action
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

	/**
	 * @param state a state of the environment
	 * @return the list of possible actions for this state
	 */
	public List<IAction> getPossibleActions(IState state);
}

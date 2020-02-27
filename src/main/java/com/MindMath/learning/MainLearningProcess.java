/**
 *
 */
package com.MindMath.learning;

import java.util.List;
import java.util.Map;

import com.MindMath.learning.algorithms.QLearning;
import com.MindMath.learning.policies.EpsilonGreedy;
import com.MindMath.learning.policies.Greedy;
import com.MindMath.learning.policies.IPolicy;
import com.MindMath.learning.utils.actions.IAction;
import com.MindMath.learning.utils.states.IState;
import com.MindMath.learning.utils.values.IValue;
import com.MindMath.learning.ztest.Grille;
import com.MindMath.learning.ztest.GrilleAction;
import com.MindMath.learning.ztest.TypeEtat;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MainLearningProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Comment to test
//		if (true)
//			return;

		Grille testEnv = new Grille();
//		IPolicy policy = new Boltzmann(0.5);
		IPolicy policy = new EpsilonGreedy(0.7);
		QLearning qLearning = new QLearning(policy, testEnv.getStates(), testEnv.getActions());

		for (int i = 0; i < 50; i++) {
			System.out.println("----------\nITERATION " + (i + 1) + "\n----------");

			testEnv.reset();
			IState state = testEnv.getCurrentState();
			int step = 0;
			while (/* step <= Grille.nbMaxActions && */ testEnv.getCurrentState().getType() != TypeEtat.Goal) {
				IAction action = qLearning.step(state);
				double reward = testEnv.step((GrilleAction) action);

				IState newState = testEnv.getCurrentState();

				qLearning.learn(state, action, reward, newState);

//				System.out.println("Step " + step + " : from " + state + " go to " + action + " arrives in " + newState
//						+ " (reward : " + reward + ")");

				state = newState;
				step++;
			}
		}

		Map<IState, List<IValue>> qValues = qLearning.getQValues();
		String res = "";

		for (IState state : testEnv.getStates()) {
			String line = state + ";";

			for (IValue value : qValues.get(state)) {
				line += value.myAction() + "→" + value.getValue() + ";";
			}

			res += "\n" + line;
		}

		System.out.println(res);

		qLearning.setExplorationPolicy(new Greedy());
		testEnv.reset();
		IState state = testEnv.getCurrentState();
		String str = "";
		int reward = 0;
		while (testEnv.getCurrentState().getType() != TypeEtat.Goal) {
			IAction action = qLearning.step(state);
			reward += testEnv.step((GrilleAction) action);

			str += state + "→";
			state = testEnv.getCurrentState();

//			System.out.println(str);
		}

		str += state.toString();

		System.out.println(str);
		System.out.println(reward);
	}

}

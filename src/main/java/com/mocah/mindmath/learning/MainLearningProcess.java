/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.policies.EpsilonGreedy;
import com.mocah.mindmath.learning.policies.Greedy;
import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.learning.ztest.Grille;
import com.mocah.mindmath.learning.ztest.GrilleAction;
import com.mocah.mindmath.learning.ztest.TypeEtat;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MainLearningProcess {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Gson gson = new Gson();
		try (Reader reader = new FileReader("test.json")) {

			// Convert JSON File to Java Object
			Tree tree = gson.fromJson(reader, Tree.class);

			// print tree
			System.out.println(tree);

		} catch (IOException e) {
			e.printStackTrace();
		}

//		try {
//			String trigger = "sally";
//			FileInputStream is = new FileInputStream("test.pl");
//			Theory tr = new Theory(is);
//			Prolog pg = new Prolog();
//			pg.setTheory(tr);
//
////			SolveInfo info = pg.solve("frere_ou_soeur(sally, A).");
////			SolveInfo info = pg.solve("pere(tom, " + trigger + ").");
//			SolveInfo info = pg.solve("equal(sally, " + trigger + ").");
//			while (info.isSuccess()) {
//				System.out.println("solution: " + info.getSolution() + " - bindings: " + info);
//				System.out.println(info.getSolution().getTerm());
//				if (pg.hasOpenAlternatives()) {
//					info = pg.solveNext();
//				} else {
//					break;
//				}
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (InvalidTheoryException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (MalformedGoalException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (NoSolutionException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (NoMoreSolutionException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		}

		// Comment to test
		if (true)
			return;

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

	/**
	 * Init the learning process based on data given
	 *
	 * <ul>
	 * <li>Should get an existing learning (or at least learned values)
	 * <li>Instantiate a learning
	 * <li>Populate with default data (for empty ones) -> ie compute states from
	 * decision tree
	 * </ul>
	 */
	public void initLearning() {
		// TODO
	}
}

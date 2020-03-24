/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.mocah.mindmath.decisiontree.Branch;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.NodeType;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.decisiontree.search.DeepFirstSearch;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.policies.EpsilonGreedy;
import com.mocah.mindmath.learning.policies.Greedy;
import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.Action;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.states.State;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.learning.utils.values.QValue;
import com.mocah.mindmath.learning.ztest.Grille;
import com.mocah.mindmath.learning.ztest.GrilleAction;
import com.mocah.mindmath.learning.ztest.TypeEtat;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class MainLearningProcess {

	private static void decisionTreeDFS(Tree tree) {
		DeepFirstSearch dfs = new DeepFirstSearch(tree);

		List<Branch> branches = new ArrayList<>();

		Node node = tree.getRoot();
		goDeep(dfs, node, branches, new Branch());

		// TODO for test purpose
		System.out.println("Visit order: " + dfs.getVisitedNodes());
		System.out.println("Computed branches : " + branches);

		computeBranches(branches);
	}

	private static void goDeep(DeepFirstSearch dfs, Node node, List<Branch> branches, Branch currentBranch) {
		if (node != null) {
			dfs.visitNode(node);

			switch (node.getType()) {
			case STATE:
				currentBranch.addStateNode(node);
				break;
			case DECISION:
				// We reach a decision node put de branch in list
				currentBranch.setDecisionNode(node);
				branches.add(currentBranch);
				break;
			case FEEDBACK:
				currentBranch.addFeedbackNode(node);
				break;
			default:
				// TODO Unknown node
				break;
			}

			Deque<Node> opened = dfs.open(node);

			if (opened.isEmpty()) {
				//
			} else {
				while (!opened.isEmpty()) {
					Node child = opened.pollFirst();

					Branch extbranch = currentBranch;
					if (node.getType() == NodeType.STATE) {
						extbranch = new Branch(currentBranch);
					}

					goDeep(dfs, child, branches, extbranch);
				}
			}
		}
	}

	private static void computeBranches(List<Branch> branches) {
		Map<IState, List<IValue>> qValues = new HashMap<>();

		for (Branch branch : branches) {
			computeBranch(qValues, branch);
		}

		System.out.println(qValues);

		String res = "\n";

		for (IState state : qValues.keySet()) {
			String line = state + ";";

			for (IValue value : qValues.get(state)) {
				line += value.myAction() + "→" + value.getValue() + ";";
			}

			res += "\n" + line;
		}

		System.out.println(res);
	}

	private static void computeBranch(Map<IState, List<IValue>> qValues, Branch branch) {
		List<Node> state = branch.getStateNodes();
		Node decision = branch.getDecisionNode();
		List<Node> feedbacks = branch.getFeedbackNodes();

		State s = new State();

		state.add(decision);
		for (int i = 0; i < state.size() - 1; i++) {
			Node node = state.get(i);
			Node child = state.get(i + 1);

			s.putParam(node.getId(), node.getChild(child).getEdge().getValue());
		}

		List<IValue> values = new ArrayList<>();
		for (Node node : feedbacks) {
			IAction action = new Action(node.getFeedbackId());
			IValue qvalue = new QValue(action, decision.getChild(node).getEdge().getValue().getAsDouble());

			values.add(qvalue);
		}

		qValues.put(s, values);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tree tree = null;
		Gson gson = new Gson();
		try (Reader reader = new FileReader("test.json")) {

			// Convert JSON File to Java Object
			tree = gson.fromJson(reader, Tree.class);

			// print tree
			System.out.println(tree);

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (tree != null) {
			decisionTreeDFS(tree);
		}

		try {
			String trigger = "sally";
			FileInputStream is = new FileInputStream("test.pl");
			Theory tr = new Theory(is);
			Prolog pg = new Prolog();
			pg.setTheory(tr);

//			SolveInfo info = pg.solve("frere_ou_soeur(sally, A).");
//			SolveInfo info = pg.solve("pere(tom, " + trigger + ").");
			SolveInfo info = pg.solve("equal(sally, " + trigger + ").");
			while (info.isSuccess()) {
				System.out.println("solution: " + info.getSolution() + " - bindings: " + info);
				System.out.println(info.getSolution().getTerm());
				if (pg.hasOpenAlternatives()) {
					info = pg.solveNext();
				} else {
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (InvalidTheoryException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (MalformedGoalException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (NoSolutionException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		} catch (NoMoreSolutionException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		// Comment to test qlearning on simple env
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

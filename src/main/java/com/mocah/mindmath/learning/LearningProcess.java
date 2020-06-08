/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.mocah.mindmath.decisiontree.Branch;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.NodeType;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.decisiontree.search.DeepFirstSearch;
import com.mocah.mindmath.learning.algorithms.ILearning;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.policies.EpsilonGreedy;
import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.Action;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.states.State;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.learning.utils.values.QValue;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class LearningProcess {
	/**
	 *
	 */
	// TODO other way of storage -> due to an idea of using multiple Qlearning
	// instances (ie one for each domain/generator/familly...)
	private static ILearning learning;

	/**
	 *
	 */
	public static void initLearningProcess() {
		// 1 Check/restore existing values
		// TODO after export/import system done

		// 2 Read decision tree
		Reader reader = LocalRouteRepository.readFileasReader(LocalRoute.DecisionTreeRoute);
		// convert JSON File to Java Object
		Gson gson = new Gson();
		Tree tree = gson.fromJson(reader, Tree.class);
		// get branches from decision tree
		List<Branch> branches = decisionTreeDFS(tree);
		// compute branches to qvalues
		Map<IState, List<IValue>> qvalues = computeBranches(branches);

		// 3 Compute
		if (learning == null) {
			// Instanciate learning
			IPolicy newPolicy = new EpsilonGreedy(0.6);
			learning = new QLearning(newPolicy, qvalues);
			// or
//			learning = new QLearning(newPolicy, qvalues, alpha, gamma);
		} else {
			// Add missing states to learning class
			if (learning instanceof QLearning) {
				Map<IState, List<IValue>> trainedValues = ((QLearning) learning).getQValues();

				Set<IState> newStates = new HashSet<>(qvalues.keySet());
				newStates.removeIf(trainedValues.keySet()::contains);

				for (IState newState : newStates) {
					trainedValues.put(newState, trainedValues.get(newState));
				}
			} else {
				// TODO error
			}
		}
	}

	/**
	 * Same as calling <code>makeDecision(task, null, null)</code>
	 * 
	 * @param task
	 */
	public static void makeDecision(Task task) {
		makeDecision(task, null, null);
	}

	/**
	 * @param task
	 * @param previousTask
	 * @param previousAction
	 * @return
	 */
	public static IAction makeDecision(Task task, Task previousTask, IAction previousAction) {
		// 1 generate current state
		IState newState = null;
		// TODO

		// 2 calc reward
		if (previousTask != null) {
			// generate previous task
			IState oldState = null;
			// TODO
			// calc reward
			double reward = 0;
			// TODO
			// learn
			learning.learn(oldState, previousAction, reward, newState);
		}

		// 3 choose action
		IAction action = learning.step(newState);

		return action;
	}

	/**
	 * @param tree
	 * @return
	 */
	private static List<Branch> decisionTreeDFS(Tree tree) {
		DeepFirstSearch dfs = new DeepFirstSearch(tree);

		List<Branch> branches = new ArrayList<>();

		Node node = tree.getRoot();
		goDeep(dfs, node, branches, new Branch());

		return branches;
	}

	/**
	 * @param dfs
	 * @param node
	 * @param branches
	 * @param currentBranch
	 */
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

	/**
	 * @param branches
	 * @return
	 */
	private static Map<IState, List<IValue>> computeBranches(List<Branch> branches) {
		Map<IState, List<IValue>> qValues = new HashMap<>();

		for (Branch branch : branches) {
			computeBranch(qValues, branch);
		}

		return qValues;
	}

	/**
	 * @param qValues
	 * @param branch
	 */
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
}

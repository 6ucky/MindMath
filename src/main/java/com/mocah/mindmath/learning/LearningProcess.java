/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.mocah.mindmath.decisiontree.Branch;
import com.mocah.mindmath.decisiontree.Child;
import com.mocah.mindmath.decisiontree.Edge;
import com.mocah.mindmath.decisiontree.Node;
import com.mocah.mindmath.decisiontree.NodeType;
import com.mocah.mindmath.decisiontree.Tree;
import com.mocah.mindmath.decisiontree.Vars;
import com.mocah.mindmath.decisiontree.search.BreadthFirstSearch;
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
import com.mocah.mindmath.repository.jxapi.Actor;
import com.mocah.mindmath.repository.jxapi.Statement;
import com.mocah.mindmath.repository.jxapi.StatementResult;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepository;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Params;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;
import com.mocah.mindmath.server.cabri.jsondata.Task;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;

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

	private static Tree tree;

	/**
	 *
	 */
	public static ILearning initLearningProcess() {
		// 1 Check/restore existing values
		// TODO after export/import system done

		// 2 Read decision tree
		Reader reader = LocalRouteRepository.readFileasReader(LocalRoute.DecisionTreeRoute);
		// convert JSON File to Java Object
		Gson gson = new Gson();
		tree = gson.fromJson(reader, Tree.class);
		// get branches from decision tree
		List<Branch> branches = decisionTreeDFS(tree);
		// compute branches to qvalues
		Map<IState, ArrayList<IValue>> qvalues = computeBranches(branches);

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
				Map<IState, ArrayList<IValue>> trainedValues = ((QLearning) learning).getQValues();

				Set<IState> newStates = new HashSet<>(qvalues.keySet());
				newStates.removeIf(trainedValues.keySet()::contains);

				for (IState newState : newStates) {
					trainedValues.put(newState, trainedValues.get(newState));
				}
			} else {
				// TODO error
			}
		}

		return learning;
	}

	/**
	 * Same as calling {@code makeDecision(task, null, null)}
	 *
	 * @param task
	 * @throws IOException
	 * @throws MalformedGoalException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws InvalidTheoryException
	 */
	public static IAction makeDecision(Task task) throws InvalidTheoryException, NoSuchFieldException,
			NoSuchMethodException, InvocationTargetException, MalformedGoalException, IOException {
		return makeDecision(task, null, null);
	}

	/**
	 * @param task
	 * @param previousTask
	 * @param previousAction
	 * @return
	 * @throws IOException
	 * @throws MalformedGoalException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws InvalidTheoryException
	 */
	public static IAction makeDecision(Task task, Task previousTask, IAction previousAction)
			throws InvalidTheoryException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException,
			MalformedGoalException, IOException {
		// 1 generate current state
		IState newState = null;
		newState = decisionTreeBFS(tree, task);

		// TODO check if newState exist in Qtable else try to get the most similar state

		// 2 calc reward
		if (previousTask != null) {
			// generate previous task
			IState oldState = null;
			oldState = decisionTreeBFS(tree, previousTask);

			// TODO check if oldState exist in Qtable else try to get the most similar state

			// calc reward
			double reward = calcReward(task);

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
	private static Map<IState, ArrayList<IValue>> computeBranches(List<Branch> branches) {
		Map<IState, ArrayList<IValue>> qValues = new HashMap<>();

		for (Branch branch : branches) {
			computeBranch(qValues, branch);
		}

		return qValues;
	}

	/**
	 * @param qValues
	 * @param branch
	 */
	private static void computeBranch(Map<IState, ArrayList<IValue>> qValues, Branch branch) {
		List<Node> state = branch.getStateNodes();
		Node decision = branch.getDecisionNode();
		List<Node> feedbacks = branch.getFeedbackNodes();

		State s = new State();

		state.add(decision);
		for (int i = 0; i < state.size() - 1; i++) {
			Node node = state.get(i);
			Node child = state.get(i + 1);

			JsonPrimitive val = node.getChild(child).getEdge().getValue();
			if (val.isNumber()) {
				s.putParam(node.getId(), val.getAsNumber());
			} else if (val.isBoolean()) {
				s.putParam(node.getId(), val.getAsBoolean());
			} else {
				s.putParam(node.getId(), val.getAsString());
			}
		}

		ArrayList<IValue> values = new ArrayList<>();
		for (Node node : feedbacks) {
			IAction action = new Action(node.getFeedbackId());
			IValue qvalue = new QValue(action, decision.getChild(node).getEdge().getValue().getAsDouble());

			values.add(qvalue);
		}

		qValues.put(s, values);
	}

	private static IState decisionTreeBFS(Tree tree, Task task) throws IOException, InvalidTheoryException,
			NoSuchFieldException, NoSuchMethodException, InvocationTargetException, MalformedGoalException {
		BreadthFirstSearch bfs = new BreadthFirstSearch(tree);

		Node node = tree.getRoot();
		State state = new State();

		InputStream input = LocalRouteRepository.readFileasInputStream(LocalRoute.PrologTestRoute);

		Prolog pg;
		try {
			Theory tr = new Theory(input);
			pg = new Prolog();
			pg.setTheory(tr);
		} catch (IOException e) {
			throw new IOException("Missing Prolog file; file path should be '" + LocalRoute.PrologTestRoute
					+ "'. Read access to file could be missing to.", e);
//			e.printStackTrace();
		}

		if (pg != null) {
			stateInterprete(bfs, node, task, state, pg);
		}

		return state;
	}

	private static void stateInterprete(BreadthFirstSearch bfs, Node node, Task task, State state, Prolog pg)
			throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, MalformedGoalException {
		if (node != null) {
			bfs.visitNode(node);

			switch (node.getType()) {
			case STATE:
				Deque<Node> opened = bfs.open(node);

				if (opened.isEmpty()) {
					//
				} else {
					while (!opened.isEmpty()) {
						Node child = opened.pollFirst();
						System.out.println("TEST edge to " + child.getId());

						Child c = node.getChild(child);
						Edge e = c.getEdge();

						String query = e.getQuery();
						for (Vars var : e.getVars()) {
							String replacement = "ERROR";

							switch (var.getSource()) {
							case LOG:
								List<Log> logs = task.getLog();
								break;
							case PARAM:
								Params params = task.getParams();
								replacement = Extractor.getFromParams(params, var.getKey());
								break;
							case SENSOR:
								Sensors sensors = task.getSensors();
								replacement = Extractor.getFromSensors(sensors, var.getKey());
								break;
							case TASK:
								replacement = Extractor.getFromTask(task, var.getKey());
								break;
							case CUSTOM_METHOD:
								replacement = Extractor.getFromMethod(task, var.getKey());
								break;
							default:
								break;
							}

							query = query.replaceFirst("_VAR_", replacement);
						}

						System.out.println(query);
						SolveInfo info = pg.solve(query);

						if (info.isSuccess()) {
							JsonPrimitive val = e.getValue();
							if (val.isNumber()) {
								state.putParam(node.getId(), val.getAsNumber());
							} else if (val.isBoolean()) {
								state.putParam(node.getId(), val.getAsBoolean());
							} else {
								state.putParam(node.getId(), val.getAsString());
							}

							stateInterprete(bfs, child, task, state, pg);

							break;
						}
					}
				}
				break;

			case DECISION:
			case FEEDBACK:
				// TODO
				break;

			default:
				// TODO Unknown node
				break;
			}

		}
	}

	/**
	 * @param task
	 * @return
	 */
	private static double calcReward(Task task) {
		double reward = 0;
		double baseReward = 100;

		String answer = task.getSensors().isCorrectAnswer();
		Boolean b = BooleanUtils.toBooleanObject(answer);

		if (b == null)
			// No answer -> ask help case
			return reward;

		reward = baseReward * (1 / feedbackInfo(task)); // TODO check case feedbackInfo return 0

		if (!b) {
			// Answer was false -> negative reward
			reward = -(baseReward - reward);
		} else {
			// Answer was true -> positive reward
		}

		// Keep reward between -baseReward and baseReward
		if (reward > baseReward) {
			reward = baseReward;
		}
		if (reward < -baseReward) {
			reward = -baseReward;
		}

		return reward;
	}

	/**
	 * Calc (sum_infos_send/feedback_number)
	 *
	 * @param task
	 * @return
	 */
	private static double feedbackInfo(Task task) {
		double feedbackInfo = 0;

		List<String> feedbacks = getFeedbacks(task);

		double sum = 0;
		for (String feedbackId : feedbacks) {
			sum += getWeightInfo(feedbackId);
		}

		// TODO check for case divide by 0 -> shouldn't occurs, but for security purpose
		feedbackInfo = sum / feedbacks.size();

		return feedbackInfo;
	}

	/**
	 * Get all the feedbacks sent to a learner (got from Task object) for a same
	 * family task.
	 *
	 * @param task
	 * @return
	 */
	private static List<String> getFeedbacks(Task task) {
		List<String> feedbacks = new ArrayList<>();

		// TODO ask LRS and add all feedbackID for statements of same learner and family
		// task to feedbacks List

		Actor learner = task.getLearnerAsActor();

		LearningLockerRepository lrs = new LearningLockerRepository();
		try {
			lrs = lrs.filterByActor(learner);
		} catch (UnsupportedEncodingException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		// TODO wait for final statement design for filter
		lrs = lrs.addFilter("path.to.familytask", task.getSensors().getTaskFamily());

		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();

		for (Statement statement : statements) {
			String feedback = "";

			// TODO get feedbackid from the statement
			// Depends on the structure version: will the statement be unique (one statement
			// for feedback demand and answer) or separated in two statements (one statement
			// for feedback demand and another statement from feedback answer) ?
			statement.getAttachments();

			feedbacks.add(feedback);
		}

		// temp
		feedbacks.add("0.0.0.0");

		return feedbacks;
	}

	/**
	 * Get or calc the weight of information a feedback give to a learner
	 *
	 * @param feedbackId
	 * @return
	 */
	private static double getWeightInfo(String feedbackId) {
		// TODO get the weight of informations given by the feedback from Benjamin DB

		return 1;
	}
}

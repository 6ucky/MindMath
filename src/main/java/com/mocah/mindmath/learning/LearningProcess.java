/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.actions.MindMathAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.states.State;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.learning.utils.values.QValue;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryAggregation;
import com.mocah.mindmath.server.entity.task.Log;
import com.mocah.mindmath.server.entity.task.Params;
import com.mocah.mindmath.server.entity.task.Sensors;
import com.mocah.mindmath.server.entity.task.Task;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import gov.adlnet.xapi.model.Result;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;

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
	 * The default value used to init all qValues for a state. Each qValue will use
	 * the weighting (which generally sum to 1) from the decision tree and take the
	 * result value of {@code base_weight*LearningProcess.BASE_QVALUE_SCORESUM_INIT}
	 *
	 */
	public final static double BASE_QVALUE_SCORESUM_INIT = 100;

	/**
	 * @return
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
	 * @return
	 */
	public static ILearning getLearning() {
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
	public static Decision makeDecision(Task task) throws InvalidTheoryException, NoSuchFieldException,
			NoSuchMethodException, InvocationTargetException, MalformedGoalException, IOException {
		return makeDecision(task, null, null);
	}

	/**
	 * @param task
	 * @param prevState
	 * @param prevAction
	 * @return
	 * @throws IOException
	 * @throws MalformedGoalException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 * @throws InvalidTheoryException
	 */
	public static Decision makeDecision(Task task, IState prevState, IAction prevAction)
			throws InvalidTheoryException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException,
			MalformedGoalException, IOException {
		Decision decision = new Decision();

		long starttime = System.nanoTime();
		// 1 generate current state
		IState newState = null;
		newState = decisionTreeBFS(tree, task);
		System.out.println("[Decision] State detected : " + newState);
		System.out.println(
				"Time interprete current state : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));

		// TODO check if newState exist in Qtable else try to get the most similar state

		// 2 calc reward
		if (prevState != null) {
			// generate previous task
			System.out.println("[Decision] Previous state detected : " + prevState);

			// TODO check if prevState exist in Qtable else try to get the most similar
			// state

			starttime = System.nanoTime();
			// calc reward
			double reward = calcReward(task);
			decision.setReward(reward);
			System.out.println("Time calc reward : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));
			System.out.println("[Decision] Reward from action " + prevAction + " is : " + reward);

			// learn
			starttime = System.nanoTime();
			learning.learn(prevState, prevAction, reward, newState);
			System.out.println("Time apply reward : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));

			if (learning instanceof QLearning) {
				Map<IState, ArrayList<IValue>> trainedValues = ((QLearning) learning).getQValues();
				ArrayList<IValue> values = trainedValues.get(prevState);
				decision.setModifiedQvalues(prevState, values);
				System.out.println("[Decision] qvalues for this state updated " + values);
			}
		}

		// 3 choose action
		starttime = System.nanoTime();
		IAction action = null;
		if (task.isExpertMode()) {
			// Choose action based on decision tree weights
			List<IAction> actions = learning.getPossibleActions(newState);
			int actionsCount = actions.size();

			List<IAction> mostWeightedActions = new ArrayList<>();
			// Get actions(s) with max weight
			double bestWeight = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < actionsCount; i++) {
				if (actions.get(i) instanceof MindMathAction) {
					double actionWeight = ((MindMathAction) actions.get(i)).getInitalWeight();

					if (actionWeight > bestWeight) {
						// We find a better weight, clear previous actions stored and add to list
						mostWeightedActions.clear();
						bestWeight = actionWeight;

						mostWeightedActions.add(actions.get(i));
					} else if (actionWeight == bestWeight) {
						// We find another action with current best weight, add to list
						mostWeightedActions.add(actions.get(i));
					} else {
						// Action have lower weight, ignore
					}
				}
			}

			Random rand = new Random();
			// Take an action between most weighted actions
			int i = rand.nextInt(mostWeightedActions.size());
			action = mostWeightedActions.get(i);

			if (action == null) {
				// If no action chosen, choose a random action from initial list
				i = rand.nextInt(actionsCount);
				action = actions.get(i);
			}

//			// Begin make actions weights being probabilities sum at 1
//			List<Double> actionProb = new ArrayList<>();
//			double sum = 0;
//
//			for (int i = 0; i < actionsCount; i++) {
//				if (actions.get(i) instanceof MindMathAction) {
//					actionProb.add(((MindMathAction) actions.get(i)).getInitalWeight());
//					sum += actionProb.get(i);
//				}
//			}
//
//			if (sum > 0) {
//				for (int i = 0; i < actionProb.size(); i++) {
//					actionProb.set(i, actionProb.get(i) / sum);
//				}
//			}
//			// End
//
//			// Begin discrete prob
//			Random rand = new Random();
//			double d = rand.nextDouble();
//
//			double[] cumprob = new double[actionProb.size() + 1];
//			cumprob[0] = 0;
//			double total = 0;
//			for (int i = 0; i < actionProb.size(); i++) {
//				total += actionProb.get(i);
//				cumprob[i + 1] = total;
//			}
//
//			for (int i = 0; i < actionProb.size(); i++) {
//				if (d > cumprob[i] && d <= cumprob[i + 1]) {
//					action = actions.get(i);
//					break;
//				}
//			}
//			// End discrete prob
//
//			if (action == null) {
//				//If no action chosen, choose a random action from initial list
//				int i = rand.nextInt(actionsCount);
//				action = actions.get(i);
//			}
		} else {
			// Choose action based on policy
			action = learning.step(newState);
		}
		decision.setAction(action);
		System.out.println("Time taking action : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));

		System.out.println("[Decision] Action decided : " + action);

		return decision;
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
			double defaultWeight = decision.getChild(node).getEdge().getValue().getAsDouble();
			MindMathAction action = new MindMathAction(node.getFeedbackId(), s, decision.getId(), defaultWeight);

			IValue qvalue = new QValue(action, defaultWeight * LearningProcess.BASE_QVALUE_SCORESUM_INIT);

			values.add(qvalue);
		}

		qValues.put(s, values);
	}

	/**
	 * @param tree
	 * @param task
	 * @return
	 * @throws IOException
	 * @throws InvalidTheoryException
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws MalformedGoalException
	 */
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
		}

		if (pg != null) {
			Map<Vars, String> varcache = new HashMap<>();
			stateInterprete(bfs, node, task, state, pg, varcache);
		}

		return state;
	}

	/**
	 * Recursive method
	 *
	 * @param bfs
	 * @param node
	 * @param task
	 * @param state
	 * @param pg
	 * @param varcache
	 * @throws NoSuchFieldException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws MalformedGoalException
	 */
	private static void stateInterprete(BreadthFirstSearch bfs, Node node, Task task, State state, Prolog pg,
			Map<Vars, String> varcache)
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
//						System.out.println("TEST edge to " + child.getId());

						Child c = node.getChild(child);
						Edge e = c.getEdge();

						String query = StringUtils.appendIfMissing(e.getQuery(), ".");
						for (Vars var : e.getVars()) {
							String replacement = "ERROR";

							if (varcache.containsKey(var)) {
								replacement = varcache.get(var);
							} else {
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

								varcache.put(var, replacement);
							}

//							System.out.println(var);
//							System.out.println(varcache);
							query = query.replaceAll("_" + var.getName() + "_", replacement);
						}

//						System.out.println(query);
						SolveInfo info;
						try {
							info = pg.solve(query);
						} catch (MalformedGoalException mge) {
							System.out.println("[Decision] Malformed Goal " + query);
							throw mge;
						}

						if (info.isSuccess()) {
							JsonPrimitive val = e.getValue();
							if (val.isNumber()) {
								state.putParam(node.getId(), val.getAsNumber());
							} else if (val.isBoolean()) {
								state.putParam(node.getId(), val.getAsBoolean());
							} else {
								state.putParam(node.getId(), val.getAsString());
							}

							stateInterprete(bfs, child, task, state, pg, varcache);

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

		double feedbackInfo = feedbackInfo(task);
		if (feedbackInfo > 0) {
			reward = baseReward * (1 / feedbackInfo);
		} else {
			reward = baseReward;
		}

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
		double minFeedbackWeight = 1;
		double feedbackInfo = 0;

		List<String> feedbacks = getFTFeedbacks(task);

		double sum = 0;
		int i = 1;
		for (String feedbackId : feedbacks) {
			sum += minFeedbackWeight + ((getWeightInfo(feedbackId) - minFeedbackWeight) * 1 / Math.sqrt(i));

			i++;
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
	private static List<String> getFTFeedbacks(Task task) {
		List<String> feedbacks = new ArrayList<>();

		LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(task.isUsingTestLRS());

		// Filter applied : same learner and same task family
		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("learner_id", task.getSensors().getId_learner());
		scopes.put("family_task", task.getSensors().getTaskFamily());
		scopes.put("no_gaming", "true");
		scopes.put("has_feedback", "true");

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");
		try {
			mustache.execute(writer, scopes).flush();
			String query = writer.toString();
			lrs = lrs.filterByMatcher(query).limitResults(5).addPipelineStage("$sort", "{\"timestamp\":-1}");
		} catch (IOException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();

		for (Statement statement : statements) {
			Result r = statement.getResult();
			if (r != null) {
				JsonObject extensions = r.getExtensions();
				if (extensions != null) {
					// There is a feedback
					String feedback = extensions.getAsJsonObject("https://mindmath.lip6.fr/feedback").get("idFeedback")
							.getAsString();
					feedbacks.add(feedback);
				}
			}
		}

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

		// temp
		switch (feedbackId) {
		case "0.0.0.0":
		default:
			return 1;

		case "1.0.0.0":
		case "1.1.GC.0":
		case "1.1.GNC.0":
		case "2.0.0.XE":
		case "2.0.0.XFT":
		case "2.1.GNC.XE":
			return 2;

		case "1.2.IC.0":
		case "1.2.INC.0":
		case "3.0.0.XE":
		case "3.0.0.XFT":
			return 3;

		case "3.2.IC.0":
		case "3.2.INC.0":
		case "3.2.IC.XE":
		case "3.2.IC.XFT":
		case "3.2.INC.XE":
		case "3.2.INC.XFT":
			return 4;
		}
	}
}

/**
 *
 */
package com.mocah.mindmath.learning;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
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
import com.mocah.mindmath.learning.policies.Greedy;
import com.mocah.mindmath.learning.policies.IPolicy;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.actions.MindMathAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.states.State;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.learning.utils.values.QValue;
import com.mocah.mindmath.learning.ztest.Grille;
import com.mocah.mindmath.learning.ztest.GrilleAction;
import com.mocah.mindmath.learning.ztest.TypeEtat;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.owlparser.OWLparserRepo;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.entity.task.Log;
import com.mocah.mindmath.server.entity.task.Params;
import com.mocah.mindmath.server.entity.task.Sensors;
import com.mocah.mindmath.server.entity.task.Task;

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
public class TestLearningProcess {

	private static void decisionTreeDFS(Tree tree) {
		DeepFirstSearch dfs = new DeepFirstSearch(tree);

		List<Branch> branches = new ArrayList<>();

		Node node = tree.getRoot();
		goDeepDFS(dfs, node, branches, new Branch());

		// TODO for test purpose
		System.out.println("Visit order: " + dfs.getVisitedNodes());
		System.out.println("Computed branches : " + branches);

		computeBranches(branches);
	}

	private static void goDeepDFS(DeepFirstSearch dfs, Node node, List<Branch> branches, Branch currentBranch) {
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

					goDeepDFS(dfs, child, branches, extbranch);
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

		StringBuilder res = new StringBuilder();

		for (IState state : qValues.keySet()) {
			StringBuilder line = new StringBuilder();
			line.append(state);
			line.append(";");

			for (IValue value : qValues.get(state)) {
				line.append(value.myAction());
				line.append("→");
				line.append(value.getValue());
				line.append(";");
			}

			line.append("\n");

			res.append(line);
		}

		System.out.println(res.toString());
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

			JsonPrimitive val = node.getChild(child).getEdge().getValue();
			if (val.isNumber()) {
				s.putParam(node.getId(), val.getAsNumber());
			} else if (val.isBoolean()) {
				s.putParam(node.getId(), val.getAsBoolean());
			} else {
				s.putParam(node.getId(), val.getAsString());
			}
		}

		List<IValue> values = new ArrayList<>();
		for (Node node : feedbacks) {
			MindMathAction action = new MindMathAction(node.getFeedbackId());
			action.setLeaf(decision.getId());

			double defaultWeight = decision.getChild(node).getEdge().getValue().getAsDouble();
			IValue qvalue = new QValue(action, defaultWeight * LearningProcess.BASE_QVALUE_SCORESUM_INIT);

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

						String query = StringUtils.appendIfMissing(e.getQuery(), ".");
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

							System.out.println(var);
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
	 * @param args
	 */
	public static void main(String[] args) {
		Tree tree = null;
		Gson gson = new Gson();

		// initialize feedbackcontent
		try {
			List<FeedbackContent> feedbacks = gson
					.fromJson(LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute),
							FeedbackContentList.class)
					.getFeedbackcontentlist();
			List<Motivation> motivations = gson
					.fromJson(LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute),
							FeedbackContentList.class)
					.getMotivationlist();
			List<Glossaire> glossaires = gson.fromJson(LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute),
					FeedbackContentList.class).getGlossairelist();
		} catch (JsonSyntaxException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// initialize ontology file
		try {
			OWLparserRepo.owldata = LocalRouteRepository.readFileasString(LocalRoute.OntologyRoute);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Reader reader = LocalRouteRepository.readFileasReader(LocalRoute.DecisionTreeRoute);

		// Convert JSON File to Java Object
		tree = gson.fromJson(reader, Tree.class);

		// print tree
		System.out.println(tree);

		if (tree != null) {
			decisionTreeDFS(tree);
		}

		if (true)
			return;

		try {
			String trigger = "sally";
//			FileInputStream is = new FileInputStream("test.pl");
			InputStream input = LocalRouteRepository.readFileasInputStream(LocalRoute.PrologTestRoute);
			Theory tr = new Theory(input);
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

		Task t = new Task("1", null, null, null, null, null);
		try {
			System.out.println(t.getFieldValue("id"));
			System.out.println(t.getFieldValue("task"));
			System.out.println(t.getFieldValue("none"));
		} catch (NoSuchFieldException | SecurityException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

//		String data = "{\"id\":\"100\",\"sensors\":{\"idLearner\":\"learner1\",\"domain\":\"algebre\",\"generator\":\"resoudreEquationPremierDegre\",\"taskFamily\":\"ft3.1\",\"correctAnswer\":true,\"intermediaryResponse\":[{\"step\":1,\"correctAnswer\":true},{\"step\":2,\"correctAnswer\":false}],\"codeError\":\"ce_err5\",\"activityMode\":0},\"log\":[{\"time\":4015,\"type\":\"tool\",\"name\":\"line_tool\",\"action\":\"create\"},{\"time\":5813,\"type\":\"button\",\"name\":\"bouton-effacer\",\"action\":\"push\"},{\"time\":7689,\"type\":\"button\",\"name\":\"bouton-valider\",\"action\":\"push\"}]}";
//		String data = "{\"id\":\"100\",\"sensors\":{\"idLearner\":\"learner1\",\"domain\":\"algebre\",\"generator\":\"resoudreEquationPremierDegre\",\"taskFamily\":\"ft3.1\",\"correctAnswer\":false,\"intermediaryResponse\":[{\"step\":1,\"correctAnswer\":true},{\"step\":2,\"correctAnswer\":false}],\"codeError\":\"ce_err5\",\"activityMode\":0},\"log\":[{\"time\":4015,\"type\":\"tool\",\"name\":\"line_tool\",\"action\":\"create\"},{\"time\":5813,\"type\":\"button\",\"name\":\"bouton-effacer\",\"action\":\"push\"},{\"time\":7689,\"type\":\"button\",\"name\":\"bouton-valider\",\"action\":\"push\"}]}";
//		String data = "{\"id\":\"100\",\"sensors\":{\"idLearner\":\"learner1\",\"domain\":\"algebre\",\"generator\":\"resoudreEquationPremierDegre\",\"taskFamily\":\"ft3.1\",\"correctAnswer\":false,\"intermediaryResponse\":[{\"step\":1,\"correctAnswer\":true},{\"step\":2,\"correctAnswer\":false}],\"activityMode\":0},\"log\":[{\"time\":4015,\"type\":\"tool\",\"name\":\"line_tool\",\"action\":\"create\"},{\"time\":5813,\"type\":\"button\",\"name\":\"bouton-effacer\",\"action\":\"push\"},{\"time\":7689,\"type\":\"button\",\"name\":\"bouton-valider\",\"action\":\"push\"}]}";

		// TODO check null pointer
		String data = "{ \"id\": \"100\", \"sensors\": {\"idLearner\":\"100\", \"domain\": \"algebre\", \"generator\": \"resoudreEquationPremierDegre\", \"taskFamily\": \"ft3.1\", \"correctAnswer\": false, \"codeError\": \"ce_err5\", \"activityMode\":\"0\" }, \"log\": [ { \"time\": 4015, \"type\": \"tool\", \"name\": \"line_tool\", \"action\": \"create\" }, { \"time\": 5813, \"type\": \"button\", \"name\": \"bouton-effacer\", \"action\": \"push\" }, { \"time\": 7689, \"type\": \"button\", \"name\": \"bouton-valider\", \"action\": \"push\" } ] }";
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		System.out.println(data);

//		try {
//			jsonparser.getValueAsString(jsonparser.getObject(), JsonParserKeys.TASK_ID);
//			Task task = jsonparser.parse(data, "v1.0");
//
//			LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(true);
//
//			HashMap<String, Object> scopes = new HashMap<>();
//			scopes.put("verb_id", Verbs.answered().getId());
//			scopes.put("family_task", task.getSensors().getTaskFamily());
//			scopes.put("no_gaming", "true");
//
//			StringWriter writer = new StringWriter();
//			MustacheFactory mf = new DefaultMustacheFactory();
//			Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");
//
//			mustache.execute(writer, scopes).flush();
//
//			String query = writer.toString();
//
//			System.out.println(query);
//
//			lrs = lrs.filterByMatcher(query);
//
//			// String resultsStr = lrs.getFilteredStatementsAsString();
//			StatementResult results = lrs.getFilteredStatements();
//			List<Statement> statements = results.getStatements();
//
//			// System.out.println(resultsStr);
//			System.out.println(statements.size());
//		} catch (IOException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		} catch (JsonParserCustomException e) {
//			// TODO Bloc catch généré automatiquement
//			e.printStackTrace();
//		}

		IState readedState = null;
		try {
			jsonparser.getValueAsString(jsonparser.getObject(), JsonParserKeys.TASK_ID);
			Task task = jsonparser.parse(data, "v1.0");
			readedState = decisionTreeBFS(tree, task);

		} catch (JsonParserCustomException | InvalidTheoryException | MalformedGoalException | NoSuchFieldException
				| SecurityException | IOException | NoSuchMethodException | IllegalArgumentException
				| InvocationTargetException | NullPointerException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		System.out.println(readedState);
		System.out.println("done\n");

		ILearning learning = LearningProcess.initLearningProcess();
		Map<IState, ArrayList<IValue>> qstates = ((QLearning) learning).getQValues();
		for (IState s : qstates.keySet()) {
			if (s.equals(readedState) && readedState.equals(s)) {
				System.out.println(readedState + " equals " + s);
				System.out.println(readedState.hashCode() + " equals " + s.hashCode());
				System.out.println(qstates.get(readedState));
			}
		}
		IAction actionTest = learning.step(readedState);
		System.out.println("\nACTION choosen: " + actionTest);

		// Comment to test state equals
		if (true)
			return;

		State s1 = new State();
		s1.putParam("A", "a0");
		s1.putParam("B", "b0");

		State s2 = new State();
		s2.putParam("A", "a0");
		s2.putParam("B", "b1");

		State s3 = new State();
		s3.putParam("A", "a1");
		s3.putParam("C", "c0");

		State s4 = new State();
		s4.putParam("A", "a1");
		s4.putParam("C", "c1");

		State stest1 = new State();
		stest1.putParam("A", "a0");
		stest1.putParam("B", "b0");
		stest1.putParam("C", "c0");

		State stest2 = new State();
		stest2.putParam("A", "a0");
		stest2.putParam("B", "b0");
		stest2.putParam("C", "c1");

		State stest3 = new State();
		stest3.putParam("A", "a1");
		stest3.putParam("B", "b0");
		stest3.putParam("C", "c0");

		State stest4 = new State();
		stest4.putParam("A", "a1");
		stest4.putParam("B", "b1");
		stest4.putParam("C", "c0");

		System.out.println(stest1.equals(s1));
		System.out.println(stest1.equals(s2));
		System.out.println(stest1.equals(s3));
		System.out.println(stest1.equals(s4));
		System.out.println("");
		System.out.println(stest2.equals(s1));
		System.out.println(stest2.equals(s2));
		System.out.println(stest2.equals(s3));
		System.out.println(stest2.equals(s4));
		System.out.println("");
		System.out.println(stest3.equals(s1));
		System.out.println(stest3.equals(s2));
		System.out.println(stest3.equals(s3));
		System.out.println(stest3.equals(s4));
		System.out.println("");
		System.out.println(stest4.equals(s1));
		System.out.println(stest4.equals(s2));
		System.out.println(stest4.equals(s3));
		System.out.println(stest4.equals(s4));

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
				// 1er demande Fdbck (et suivantes)
				IAction action = qLearning.step(state);

				// 2eme demande
				double reward = testEnv.step((GrilleAction) action);

				IState newState = testEnv.getCurrentState();

				qLearning.learn(state, action, reward, newState);

//				System.out.println("Step " + step + " : from " + state + " go to " + action + " arrives in " + newState
//						+ " (reward : " + reward + ")");

				state = newState;
				step++;
			}
		}

		Map<IState, ArrayList<IValue>> qValues = qLearning.getQValues();
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

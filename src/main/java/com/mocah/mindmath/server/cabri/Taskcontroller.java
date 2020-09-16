package com.mocah.mindmath.server.cabri;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mocah.mindmath.learning.Decision;
import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.algorithms.ILearning;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.actions.MindMathAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.states.State;
import com.mocah.mindmath.learning.utils.states.StateParam;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryAggregation;
import com.mocah.mindmath.repository.learninglocker.LearningLockerRepositoryHttp;
import com.mocah.mindmath.repository.learninglocker.XAPIgenerator;
import com.mocah.mindmath.server.Derbyrepository;
import com.mocah.mindmath.server.cabri.feedback.Feedbackjson;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.entity.task.Task;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import gov.adlnet.xapi.model.Score;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verbs;
import io.swagger.annotations.ApiParam;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@RestController
@RequestMapping("/task")
public class Taskcontroller {

	@Autowired
	private Derbyrepository taskrepository;

	@Autowired
	private RedisTemplate<String, Serializable> serializableRedisTemplate;

	private static final String license_num = "mocah";

	private Gson gson = new Gson();

	// initialize feedbackContent in Derby from local Repository
	@PostConstruct
	private void insertfeedbackcontentDerby() throws JsonSyntaxException, IOException {
		getTaskrepository().deleteAll(getTaskrepository().getAllFeedbackContent());
		getTaskrepository().deleteAll(getTaskrepository().getAllGlossaire());
		getTaskrepository().deleteAll(getTaskrepository().getAllMotivation());

		List<FeedbackContent> feedbacks = gson
				.fromJson(LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute),
						FeedbackContentList.class)
				.getFeedbackcontentlist();
		for (FeedbackContent feedback : feedbacks) {
			getTaskrepository().save(feedback);
		}

		List<Motivation> motivations = gson
				.fromJson(LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute), FeedbackContentList.class)
				.getMotivationlist();
		for (Motivation motivation : motivations) {
			getTaskrepository().save(motivation);
		}

		List<Glossaire> glossaires = gson
				.fromJson(LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute), FeedbackContentList.class)
				.getGlossairelist();
		for (Glossaire glossaire : glossaires) {
			getTaskrepository().save(glossaire);
		}
		// TODO improve init system
		// ie Save learning object in DB and add initLearning with ILearning parameter
		// -> thus it can allow multiple learning instances (and of course backups !)
		// Note : each time a learn decision will be call, we will need to get the
		// correct instance from DB, pass it to makeDecision and in case it's a decision
		// with learning -> update DB (because the instance is modified : only qValues
		// attribute)
		
		// 1 Check/restore existing values
		// TODO after export/import system done
//		ILearning learningfromRedis = (ILearning) serializableRedisTemplate.opsForValue().get("learning");
//		LearningProcess.initLearningProcess(learningfromRedis);
		LearningProcess.initLearningProcess();
	}

	/**
	 * check the post request based on authorization
	 *
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if (auth.equals(license_num))
			return true;
		return false;
	}

	/**
	 * Handle POST request default version is 1.0
	 *
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(path = "", consumes = "application/json")
	public ResponseEntity<String> addtask(@RequestHeader("Authorization") String auth,
			@RequestBody @ApiParam(value = "example:{\r\n" + 
					"    \"sensors\": {\r\n" + 
					"        \"idLearner\": \"123\",\r\n" + 
					"        \"domain\": \"algebre123\",\r\n" + 
					"        \"generator\": \"resoudreEquationPremierDegre\",\r\n" + 
					"        \"taskFamily\": \"ft3.1\",\r\n" + 
					"        \"correctAnswer\": false,\r\n" + 
					"        \"codeError\": \"ce_err5\",\r\n" + 
					"        \"activityMode\": 0\r\n" + 
					"    },\r\n" + 
					"    \"log\": [\r\n" + 
					"        {\r\n" + 
					"            \"time\": 4015,\r\n" + 
					"            \"type\": \"tool\",\r\n" + 
					"            \"name\": \"line_tool\",\r\n" + 
					"            \"action\": \"create\"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"time\": 5813,\r\n" + 
					"            \"type\": \"button\",\r\n" + 
					"            \"name\": \"bouton-effacer\",\r\n" + 
					"            \"action\": \"push\"\r\n" + 
					"        },\r\n" + 
					"        {\r\n" + 
					"            \"time\": 7689,\r\n" + 
					"            \"type\": \"button\",\r\n" + 
					"            \"name\": \"bouton-valider\",\r\n" + 
					"            \"action\": \"push\"\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					"}") String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
//		return addtaskv1_0(auth, data);
		return addtaskTEST(auth, data);
	}

	/**
	 * Handle POST request in version 1.0
	 *
	 * @param data Receive JSON file as string
	 * @param auth authorization headers
	 * @return feedback message
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws NoSuchAlgorithmException
	 * @throws JsonParseCustomException
	 */
	@PostMapping(path = "/v1.0", consumes = "application/json")
	public ResponseEntity<String> addtaskv1_0(@RequestHeader("Authorization") String auth, @RequestBody String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, CabriVersion.v1_0);

		// TODO remove or comment in production, force using test LRS
		task.setUsingTestLRS(true);

		// TODO avoid consider gaming with system tasks

		long starttime = System.nanoTime();
		Task prevTask = getTaskrepository().getPreviousTask(task.getSensors().getId_learner());
		System.out.println("Time retrieve prev task : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));

		starttime = System.nanoTime();
		task = getTaskrepository().save(task);
		System.out.println("Time read actual task : " + ((double) (System.nanoTime() - starttime) / 1_000_000_000));

		// Call Q-learning algorithm
		Decision decision = null;
		try {
			if (prevTask != null && !prevTask.isExpertMode()) {
				starttime = System.nanoTime();
				IAction prevAction = prevTask.getDecisionAction();
				if (prevAction != null) {
					IState prevState = prevAction.getState();
					System.out.println("Time retrieve action & state : "
							+ ((double) (System.nanoTime() - starttime) / 1_000_000_000));

//					decision = LearningProcess.makeDecision(task, prevState, prevAction);

					if (decision.hasLearn()) {
						// Save the prev task action's reward to the prev task
						prevTask.setReward(decision.getReward());
						getTaskrepository().save(prevTask);
					}
				} else {
					// An error happened in previous iteration and prevAction is null
					decision = LearningProcess.makeDecision(task, CabriVersion.v1_0);
				}
			} else {
				decision = LearningProcess.makeDecision(task, CabriVersion.v1_0);
			}
		} catch (InvalidTheoryException e) {
			// TODO Bloc catch généré automatiquement
			// Thus mean here that content of pl file isn't valid

			e.printStackTrace();
		} catch (NoSuchFieldException | NoSuchMethodException | MalformedGoalException e) {
			// TODO Bloc catch généré automatiquement
			// Thus mean here that there should be an error with a node in json tree

			// NoSuchFieldException -> in case of Task/Sensor/Param source type when the
			// field name doesn't exist (ie typo error)

			// NoSuchMethodException -> in case of Method source type when method wasn't
			// declared in com.mocah.mindmath.learning.Extractor

			// MalformedGoalException -> the query contains an error

			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Bloc catch généré automatiquement

			// Thus mean that a method called in com.mocah.mindmath.learning.Extractor
			// thrown an error -> method should be updated

			e.printStackTrace();
		}
		// Here 'decision' should contains the action decided and reward value
//		boolean hasReward = decision.hasLearn(); // Does the decision contains a reward
//		double reward = decision.getReward(); // Reward value

		IAction action = decision.getAction();
		// Here 'action' should contains the feedback_id to send back and task's
		// interpreted state
//		String feedback_id = action.getId(); // Feedback id
//		IState state = action.getState(); // State object
		// Aslo in case of MindMathAction object, action should contains the leaf
//		String leaf = ((MindMathAction) action).getLeaf();

		// Link 'feedback_id' to 'task' (in order to know which feedback we send
		// for a task -> will be get back with 'getActionDone')
		task.setDecisionAction(action);
		getTaskrepository().save(task);

		boolean isTest = true;
		Feedbackjson feedbackjson;
		if (isTest) {
			HashMap<String, String> glossaireMap = new HashMap<>();
			glossaireMap.put("{word1}", "{definition}");
			glossaireMap.put("{word2}", "{definition}");
			feedbackjson = new Feedbackjson(task.getSensors().getId_learner(), "", task.getSensors().getTaskFamily(),
					task.getFeedback(), "{motivation here}", "{content url here}", "image", glossaireMap);
		} else {
			feedbackjson = generateFeedback("1.2.IC.0", "6", "1", task);
		}

		// TODO set statement success and completion
		boolean statement_success = true;
		boolean statement_completion = true;
		XAPIgenerator generator = new XAPIgenerator();
		Statement statement = generator.setResult(statement_success, statement_completion, feedbackjson, CabriVersion.v1_0)
				.generateStatement(task, CabriVersion.v1_0);
		// TODO validate post statement to LRS
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(task.isUsingTestLRS());
		ll.postStatement(statement);

//		return new ResponseEntity<>("feedback:" + gson.toJson(feedbackjson) + "\nstatement:" + gson.toJson(statement),
//				HttpStatus.OK);

		// Add verbose fields
		if (task.isVerbose()) {
			// Set decision mode
			if (task.isExpertMode()) {
				feedbackjson.setMode("Expert");
			} else {
				feedbackjson.setMode("RL");
			}

			// Set reward value
			if (decision.hasLearn()) {
				feedbackjson.setReward(decision.getReward());
				feedbackjson.setModifiedState(decision.getModifiedState());
				feedbackjson.setModifiedQvalues(decision.getModifiedQvalues());
			}
		}

		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}
	
	@PostMapping(path = "/v1.1", consumes = "application/json")
	public ResponseEntity<String> addtaskv1_1(@RequestHeader("Authorization") String auth, @RequestBody String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, CabriVersion.v1_0);

		task.setUsingTestLRS(true);

		Decision decision = null;
		if(task.isExpertMode())
		{
			try {
				decision = LearningProcess.makeDecision(task, CabriVersion.v1_1);
			} catch (InvalidTheoryException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException
					| MalformedGoalException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		IAction action = decision.getAction();

		task.setDecisionAction(action);
		getTaskrepository().save(task);
		
//		ILearning learning = LearningProcess.getExpertlearning();
//		serializableRedisTemplate.opsForValue().set("expertlearning", learning);

		System.out.println(action.getId() + " " + ((MindMathAction) action).getLeaf());
		boolean isTest = true;
		Feedbackjson feedbackjson = new Feedbackjson(task.getSensors().getId_learner());
		feedbackjson = generateFeedback(action.getId(), ((MindMathAction) action).getLeaf(), "1", task);;

		// TODO set statement success and completion
		boolean statement_success = true;
		boolean statement_completion = true;
		XAPIgenerator generator = new XAPIgenerator();
		Statement statement = generator.setResult(statement_success, statement_completion, feedbackjson, CabriVersion.v1_0)
				.generateStatement(task, CabriVersion.v1_0);
		// TODO validate post statement to LRS
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(task.isUsingTestLRS());
		ll.postStatement(statement);

		// Add verbose fields
		if (task.isVerbose()) {
			// Set decision mode
			if (task.isExpertMode()) {
				feedbackjson.setMode("Expert");
			} else {
				feedbackjson.setMode("RL");
			}

			// Set reward value
			if (decision.hasLearn()) {
				feedbackjson.setReward(decision.getReward());
				feedbackjson.setModifiedState(decision.getModifiedState());
				feedbackjson.setModifiedQvalues(decision.getModifiedQvalues());
			}
		}

		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}

	@PostMapping(path = "/test", consumes = "application/json")
	public ResponseEntity<String> addtaskTEST(@RequestHeader("Authorization") String auth, @RequestBody String data)
			throws JsonParserCustomException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, CabriVersion.test);

		task = getTaskrepository().save(task);
		//TODO generic method for feedback_test
		Feedbackjson feedbackjson;
		JsonParserSensor sensorparser = new JsonParserSensor(data);
		if (sensorparser.getValueAsBoolean(sensorparser.getObject(), JsonParserKeys.SENSOR_CORRECTANSWER)) {
			feedbackjson = generateFeedback("1.2.IC.0", "6", "1", task);
		} else {
			feedbackjson = new Feedbackjson(task.getSensors().getId_learner());
		}
		String feedbackID_test = jsonparser.getValueforDB(jsonparser.getObject(), "feedbackID_test");
		String motivation_leaf_test = jsonparser.getValueforDB(jsonparser.getObject(), "motivation_leaf_test");
		String erreurID_test = jsonparser.getValueforDB(jsonparser.getObject(), "erreurID_test");
		if(feedbackID_test != null && motivation_leaf_test != null && erreurID_test != null)
		{
			String[] error_list = { "1", "2", "3", "4" };
			if (getTaskrepository().getFeedbackContent(feedbackID_test, motivation_leaf_test) != null
					&& Arrays.asList(error_list).contains(erreurID_test)) {
				feedbackjson = generateFeedback(feedbackID_test, motivation_leaf_test, erreurID_test, task);
			}
		}
		boolean statement_success = true;
		boolean statement_completion = true;
		XAPIgenerator generator = new XAPIgenerator();
		Statement statement1 = new Statement();
		
		statement1 = generator.setActor(task)
				.setVerb(task)
				.setObject(task)
				.generateStatement(task, CabriVersion.test);
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(task.isUsingTestLRS());
		String id = ll.postStatement(statement1);
		Statement statement2 = new Statement();
		generator = new XAPIgenerator();
		statement2 = generator.setActorAsLip6()
				.setVerb(Verbs.responded())
				.setObject(id)
				.setResult(statement_success, statement_completion, feedbackjson, CabriVersion.test)
				.generateStatement(task, CabriVersion.test);
		ll.postStatement(statement2);

		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}
	
	@PostMapping(path = "/test/redis", consumes = "application/json")
	public ResponseEntity<String> addtaskTESTRedis(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ILearning learning = LearningProcess.getLearning();
		serializableRedisTemplate.opsForValue().set("learning", learning);
		return new ResponseEntity<String>("Saved.", HttpStatus.OK);
	}
		
	@GetMapping(path = "/test/redis", consumes = "application/json")
	public ResponseEntity<String> gettaskTESTRedis(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ILearning learningfromRedis = (ILearning) serializableRedisTemplate.opsForValue().get("learning");
		Map<IState, ArrayList<IValue>> qValues = null;

		if (learningfromRedis instanceof QLearning) {
			QLearning ql = (QLearning) learningfromRedis;
			qValues = ql.getQValues();
		}

		StringBuilder res = new StringBuilder();

		for (IState state : qValues.keySet()) {
			StringBuilder line = new StringBuilder(state.toString());
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
		
		return new ResponseEntity<String>(res.toString(), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/test/redis", consumes = "application/json")
	public ResponseEntity<String> deletetaskTESTRedis(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException, IOException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		serializableRedisTemplate.opsForValue().set("learning", null);
		return new ResponseEntity<String>("Deleted.", HttpStatus.OK);
	}

	/**
	 * Handle GET request
	 *
	 * @return all the tasks in the repository
	 */
	@GetMapping("")
	public ResponseEntity<String> getALLtask(@RequestHeader("Authorization") String auth) {
		if (!auth.equals("test"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		List<Task> tasks = new ArrayList<>();
		getTaskrepository().getAllTask().forEach(tasks::add);
		if (tasks.size() == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
		return new ResponseEntity<>(gson.toJson(tasks), HttpStatus.FOUND);
	}

	// the default get
	@GetMapping("/v1.0")
	public ResponseEntity<String> getALLtaskv1_0(@RequestHeader("Authorization") String auth) {
		return getALLtask(auth);
	}

	/**
	 * Get actual qValues from learning
	 *
	 * @param auth
	 * @return the qvalue from learning algorithm
	 */
	@GetMapping("/qvalues")
	public ResponseEntity<String> getQValues(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ILearning learning = LearningProcess.getLearning();
		Map<IState, ArrayList<IValue>> qValues = null;

		if (learning instanceof QLearning) {
			QLearning ql = (QLearning) learning;
			qValues = ql.getQValues();
		}

		StringBuilder res = new StringBuilder();

		for (IState state : qValues.keySet()) {
			StringBuilder line = new StringBuilder(state.toString());
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

		return new ResponseEntity<>(res.toString(), HttpStatus.FOUND);
	}

	/**
	 * Handle DELETE request
	 *
	 * @return
	 */
	@DeleteMapping(path = "")
	public ResponseEntity<String> cleandatabase(@RequestHeader("Authorization") String auth) {
		if (!auth.equals("test"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		getTaskrepository().deleteAll(getTaskrepository().getAllTask());
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
	}

	// the default delete
	@DeleteMapping(path = "/v1.0")
	public ResponseEntity<String> cleandatabasev1_0(@RequestHeader("Authorization") String auth) {
		return cleandatabase(auth);
	}

	/**
	 * get task repository
	 *
	 * @return a repository interface that works with task entities
	 */
	public Derbyrepository getTaskrepository() {
		return taskrepository;
	}

	/**
	 * TODO get feedbackID, leaf, error_code from Q-learning
	 *
	 * @param feedbackID Got from action.getId()
	 * @param leaf       Got from ((MindMathAction) action).getLeaf()
	 * @param error_code Got from Task.sensors.errorCode
	 * @param task
	 * @return feedback
	 * @throws IOException
	 */
	public Feedbackjson generateFeedback(String feedbackID, String leaf, String error_code, Task task)
			throws IOException {
		// get feedbackcontent from Derby
		FeedbackContent fb = getTaskrepository().getFeedbackContent(feedbackID, leaf);
		List<Motivation> motivations = getTaskrepository().getMotivation(fb.getMotivation_leaf());
		HashMap<String, String> glossaireMap = new HashMap<>();
		if (!fb.getContentErrorType(error_code).getGlossaire().toString().equals("[]")) {
			for (int i = 0; i < fb.getContentErrorType(error_code).getGlossaire().size(); i++) {
				String mapkey = fb.getContentErrorType(error_code).getGlossaire().get(i);
				Glossaire temp = getTaskrepository().getGlossaire(mapkey);
				glossaireMap.put(temp.getGlossaire_name(), temp.getGlossaire_content());
			}
		}
		return new Feedbackjson(task.getSensors().getId_learner(), "", task.getSensors().getTaskFamily(), feedbackID,
				motivations.get(new Random().nextInt(motivations.size())).getMotivation_data(),
				fb.getContentErrorType(error_code).getContent_url(), fb.getContentErrorType(error_code).getFormat(),
				glossaireMap);
	}

	/**
	 * Get the previous Task from database
	 *
	 * @deprecated use getTaskrepository().getPreviousTask() instead
	 * @param task the {@code Task} object from which one we want the previous task
	 * @return the previous task, {@code null} if there isn't previous Task
	 */
	@Deprecated
	public Task getPreviousTask(Task task) {
		// TODO in SQL
		List<Task> tasks = new ArrayList<>();
		getTaskrepository().getAllTask().forEach(tasks::add);

		Task previoustask = new Task("0");

		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).equals(task)) {
				continue;
			}

			if (tasks.get(i).getSensors().getId_learner().equals(task.getSensors().getId_learner())
					&& tasks.get(i).getId() > previoustask.getId()) {
				previoustask = tasks.get(i);
			}
		}

		return (!previoustask.equals(new Task("0"))) ? previoustask : null;
	}
	
	/**
	 * a method to get previous score from LRS
	 * @param statementRef_id the id of statement reference
	 * @return list of scores
	 * @throws IOException 
	 */
	public ArrayList<Score> getPreviousScore(String statementRef_id) throws IOException
	{
		LearningLockerRepositoryAggregation lrs = new LearningLockerRepositoryAggregation(true);

		HashMap<String, Object> scopes = new HashMap<>();
		scopes.put("LIP6_actor", "MOCAH");
		scopes.put("statement_reference", statementRef_id);

		StringWriter writer = new StringWriter();
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache mustache = mf.compile("mustache_template/queryAVFt.mustache");

		mustache.execute(writer, scopes).flush();

		String query = writer.toString();

		lrs = lrs.filterByMatcher(query);

		// String resultsStr = lrs.getFilteredStatementsAsString();
		StatementResult results = lrs.getFilteredStatements();
		List<Statement> statements = results.getStatements();
		Gson gson = new Gson();
		ArrayList<Score> scores = new ArrayList<Score>();
		for(Statement statement : statements)
		{
			scores.add(statement.getResult().getScore());
		}
		return scores;
	}
}

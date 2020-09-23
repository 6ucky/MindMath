package com.mocah.mindmath.server.controller.cabri;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.BooleanUtils;
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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mocah.mindmath.learning.Decision;
import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.learning.utils.actions.MindMathAction;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.server.entity.feedback.Feedbackjson;
import com.mocah.mindmath.server.entity.feedback.TaskFeedback1_1;
import com.mocah.mindmath.server.entity.feedbackContent.ErrorTypeMap;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContentList;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.entity.task.Task;
import com.mocah.mindmath.server.repository.LocalRoute;
import com.mocah.mindmath.server.repository.LocalRouteRepository;
import com.mocah.mindmath.server.repository.derby.Derbyrepository;
import com.mocah.mindmath.server.repository.learninglocker.LearningLockerRepositoryHttp;
import com.mocah.mindmath.server.repository.learninglocker.XAPIgenerator;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.MalformedGoalException;
import gov.adlnet.xapi.model.Statement;
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
		LearningProcess.setRepo(taskrepository);
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
					"        \"idLearner\": \"100\",\r\n" + 
					"        \"idTask\": \"2020-09-22-14:00:00\",\r\n" + 
					"        \"domain\": \"algebre123\",\r\n" + 
					"        \"generator\": \"resoudreEquationPremierDegre\",\r\n" + 
					"        \"taskFamily\": \"ft3.1\",\r\n" + 
					"        \"correctAnswer\": false,\r\n" + 
					"        \"codeError\": \"ce_oppose\",\r\n" + 
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
		return addtaskv1_1(auth, data);
	}

	/**
	 * Handle POST request in version 1.0 
	 *
	 * @author Thibaut SF
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
	
	/**
	 * Generate feedback
	 * @author Yan W
	 * @since 10/2020
	 * @param auth authoriazation
	 * @param data Cabri json
	 * @return feedback json
	 * @throws JsonParserCustomException Cabri json parser exception
	 * @throws IOException Read or write exception
	 * @throws NoSuchAlgorithmException Algorithm exception
	 * @throws URISyntaxException URI format exception
	 */
	@PostMapping(path = "/v1.1", consumes = "application/json")
	public ResponseEntity<String> addtaskv1_1(@RequestHeader("Authorization") String auth, @RequestBody String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		JsonParserFactory jsonparser = new JsonParserFactory(data);
		Task task = jsonparser.parse(data, CabriVersion.v1_0);

		/**
		 * @param success    true if we call decision process, false if we decide the
		 *                   cabri JSON is gaming with the system
		 * @param completion true if decision process work, false if decision produces
		 *                   an error
		 */
		boolean statement_success = false;
		boolean statement_completion = false;
		boolean correctanswer = BooleanUtils.toBoolean(task.getSensors().isCorrectAnswer());
		
		correctanswer = false;
		
		if(!correctanswer)
		{
			statement_success = true;
			statement_completion = true;
		}
		
		Decision decision = null;
		try {
			if(!correctanswer)
				decision = LearningProcess.makeDecision(task, CabriVersion.v1_1);
		} catch (InvalidTheoryException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException
				| MalformedGoalException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		ILearning learning = LearningProcess.getExpertlearning();
//		serializableRedisTemplate.opsForValue().set("expertlearning", learning);

		Feedbackjson feedbackjson;
		IAction action = null;
		if(correctanswer)
			feedbackjson = new Feedbackjson(task.getSensors().getId_learner(), task.getSensors().getId_Task(), "", 
					task.getSensors().getTaskFamily(), correctanswer, 0.8, correctanswer);
		else
		{
			action = decision.getAction();
			feedbackjson = generateFeedback(action.getId(), ((MindMathAction) action).getLeaf(), decision.getError_type(), task);;
		}

		//save task and feedback in Derby
		TaskFeedback1_1 task_fb = new TaskFeedback1_1(task.getSensors().getId_learner(), 
				task.getSensors().getId_Task(),
				task.getSensors().getDomain(),
				task.getSensors().getGenerator(),
				task.getSensors().getTaskFamily(),
				BooleanUtils.toBoolean(task.getSensors().isCorrectAnswer()),
				task.getSensors().getCodeError(),
				task.getSensors().getActivityMode(),
				task.getLog(),
				feedbackjson.getIdFeedback(),
				correctanswer ? "" : ((MindMathAction) action).getLeaf(),
				correctanswer ? ErrorTypeMap.getErrorNum(task.getSensors().getCodeError()) : decision.getError_type(),
				feedbackjson.getMotivationalElementFb(),
				feedbackjson.getContentFb(),
				feedbackjson.getGlossaryFb(),
				task.getVerb().getId(),
				statement_success,
				statement_completion,
				feedbackjson.getSuccessScore(),
				feedbackjson.isCloseTask()
				);
		getTaskrepository().save(task_fb);
		
		//generate task statement and feedback statement
		XAPIgenerator generator = new XAPIgenerator();
		Statement statement1 = new Statement();
		
		statement1 = generator.setActor(task)
				.setVerb(task)
				.setObject(task)
				.generateStatement(task, CabriVersion.v1_1);
		LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp(task.isUsingTestLRS());
		String id = ll.postStatement(statement1);
		Statement statement2 = new Statement();
		generator = new XAPIgenerator();
		statement2 = generator.setActorAsLip6()
				.setVerb(Verbs.responded())
				.setObject(id)
				.setResult(statement_success, statement_completion, feedbackjson, CabriVersion.v1_1)
				.generateStatement(task, CabriVersion.v1_1);
		ll.postStatement(statement2);

		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
	}
	
	/**
	 * @since 05/2020
	 * @author Yan W
	 * @param auth
	 * @param data
	 * @return
	 * @throws JsonParserCustomException
	 * @throws IOException
	 */
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

	/**
	 * Handle GET request
	 *
	 * @return all the tasks in the repository
	 */
	@GetMapping("")
	public ResponseEntity<String> getALLtask(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
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
	
	// the default get
	@GetMapping("/v1.1")
	public ResponseEntity<String> getALLtaskv1_1(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		List<TaskFeedback1_1> task_fb = new ArrayList<>();
		getTaskrepository().getAllTaskFeedback1_1().forEach(task_fb::add);
		if (task_fb.size() == 0)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
		return new ResponseEntity<>(gson.toJson(task_fb), HttpStatus.FOUND);
	}

	/**
	 * Handle DELETE request
	 *
	 * @return
	 */
	@DeleteMapping(path = "")
	public ResponseEntity<String> cleandatabase(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		getTaskrepository().deleteAll(getTaskrepository().getAllTask());
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
	}

	// the default delete
	@DeleteMapping(path = "/v1.0")
	public ResponseEntity<String> cleandatabasev1_0(@RequestHeader("Authorization") String auth) {
		return cleandatabase(auth);
	}
	
	// the default delete
	@DeleteMapping(path = "/v1.1")
	public ResponseEntity<String> cleandatabasev1_1(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		getTaskrepository().deleteAll(getTaskrepository().getAllTaskFeedback1_1());
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty.");
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
		return new Feedbackjson(task.getSensors().getId_learner(), "", "", task.getSensors().getTaskFamily(), feedbackID,
				motivations.get(new Random().nextInt(motivations.size())).getMotivation_data(),
				fb.getContentErrorType(error_code).getContent_url(), fb.getContentErrorType(error_code).getFormat(),
				glossaireMap, BooleanUtils.toBoolean(task.getSensors().isCorrectAnswer()), 0.8, BooleanUtils.toBoolean(task.getSensors().isCorrectAnswer()));
	}
}

package com.mocah.mindmath.server.cabri;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.utils.actions.Action;
import com.mocah.mindmath.learning.utils.actions.IAction;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.parser.jsonparser.JsonParserFactory;
import com.mocah.mindmath.parser.jsonparser.JsonParserKeys;
import com.mocah.mindmath.parser.jsonparser.JsonParserSensor;
import com.mocah.mindmath.repository.LocalRoute;
import com.mocah.mindmath.repository.LocalRouteRepository;
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
import gov.adlnet.xapi.model.Statement;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

@RestController
@RequestMapping("/task")
public class Taskcontroller {

	@Autowired
	private Derbyrepository taskrepository;

	private static final String license_num = "mocah";
	
	private boolean isInsertfeedbackContent = false;
	
	private Gson gson = new Gson();

	//initialize feedbackContent in Derby from local Repository
	private void insertfeedbackcontentDerby() throws JsonSyntaxException, IOException {
		getTaskrepository().deleteAll(getTaskrepository().getAllFeedbackContent());
		getTaskrepository().deleteAll(getTaskrepository().getAllGlossaire());
		getTaskrepository().deleteAll(getTaskrepository().getAllMotivation());
		
		List<FeedbackContent> feedbacks = gson.fromJson(LocalRouteRepository.readFileasString(LocalRoute.FeedbackContentRoute), FeedbackContentList.class).getFeedbackcontentlist();
		for (FeedbackContent feedback : feedbacks) {
			getTaskrepository().save(feedback);
		}
		
		List<Motivation> motivations = gson.fromJson(LocalRouteRepository.readFileasString(LocalRoute.MotivationRoute), FeedbackContentList.class).getMotivationlist();
		for (Motivation motivation : motivations) {
			getTaskrepository().save(motivation);
		}
		
		List<Glossaire> glossaires = gson.fromJson(LocalRouteRepository.readFileasString(LocalRoute.GlossaireRoute), FeedbackContentList.class).getGlossairelist();
		for (Glossaire glossaire : glossaires) {
			getTaskrepository().save(glossaire);
		}
		
		System.out.println("+++++++---------");
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
	 * @throws URISyntaxException 
	 * @throws NoSuchAlgorithmException 
	 */
	@PostMapping(path = "", consumes = "application/json")
	public ResponseEntity<String> addtask(@RequestHeader("Authorization") String auth, @RequestBody String data)
			throws JsonParserCustomException, IOException, NoSuchAlgorithmException, URISyntaxException {
		return addtaskv1_0(auth, data);
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
		
		if (!isInsertfeedbackContent)
		{
			insertfeedbackcontentDerby();
			isInsertfeedbackContent = true;
		}
		
		JsonParserFactory jsonparser = new JsonParserFactory(data);
		jsonparser.getValueAsString(jsonparser.getObject(), JsonParserKeys.TASK_ID);
		Task task = jsonparser.parse(data, "v1.0");

		// TODO avoid consider gaming with system tasks

		Task prevTask = getTaskrepository().getPreviousTask(task.getId_learner());
		
		getTaskrepository().save(task);

		// TODO call Q-learning algorithm

		IAction action = null;
		try {
			if (prevTask != null) {
				IAction prevAction = new Action(prevTask.getFeedback());

				action = LearningProcess.makeDecision(task, prevTask, prevAction);

			} else {
				action = LearningProcess.makeDecision(task);
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

		// Here 'action' should contains the feedback_id to send back
		String feedback_id = action.getId();

		// TODO link 'feedback_id' to 'task' (in order to know which feedback we send
		// for a task -> will be get back with 'getActionDone')
		task.setFeedback(feedback_id);
		getTaskrepository().save(task); // TODO not save but just update -> check if it's only updated

		// TODO get feedbackID, leaf, error_code from Q-learning
		String feedbackID = "1.1.GNC";
		String leaf = "11";
		String error_code = "1";

		// get feedbackcontent from Derby
		FeedbackContent fb = getTaskrepository().getFeedbackContent(feedbackID, leaf);
		List<Motivation> motivations = getTaskrepository().getMotivation(fb.getMotivation_leaf());
		HashMap<String, String> glossaireMap = new HashMap<>();
		for (int i = 0; i < fb.getContentErrorType(error_code).getGlossaire().size(); i++) {
			String mapkey = fb.getContentErrorType(error_code).getGlossaire().get(i);
			Glossaire temp = getTaskrepository().getGlossaire(mapkey);
			glossaireMap.put(temp.getGlossaire_name(), temp.getGlossaire_content());
		}
		Feedbackjson feedbackjson = new Feedbackjson(
				task.getId_learner(),
				"",
				task.getSensors().getTaskFamily(),
				feedbackID,
				motivations.get(new Random().nextInt(motivations.size())).getMotivation_data(),
				fb.getContentErrorType(error_code).getContent_url(), fb.getContentErrorType(error_code).getFormat(),
				glossaireMap);
		
		// TODO set statement success and completion
		boolean statement_success = true;
		boolean statement_completion = true;
		XAPIgenerator generator = new XAPIgenerator();
		Statement statement = generator.setResult(statement_success, statement_completion, feedbackjson).generateStatement(task);
		// TODO validate post statement to LRS
		//LearningLockerRepositoryHttp ll = new LearningLockerRepositoryHttp();
		//ll.postStatement(statement);
		
		//return new ResponseEntity<>("feedback:" + gson.toJson(feedbackjson) + "\nstatement:" + gson.toJson(statement), HttpStatus.OK);
		return new ResponseEntity<>(gson.toJson(feedbackjson), HttpStatus.OK);
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
	 * Handle DELETE request
	 *
	 * @return
	 */
	@DeleteMapping(path = "")
	public ResponseEntity<String> cleandatabase(@RequestHeader("Authorization") String auth) {
		if (!auth.equals("test"))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		getTaskrepository().deleteAll();
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

			if (tasks.get(i).getId_learner().equals(task.getId_learner())
					&& tasks.get(i).getId() > previoustask.getId()) {
				previoustask = tasks.get(i);
			}
		}

		return (!previoustask.equals(new Task("0"))) ? previoustask : null;
	}
}

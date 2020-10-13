package com.mocah.mindmath.server.controller.learning;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.algorithms.ExpertLearning;
import com.mocah.mindmath.learning.algorithms.ILearning;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.server.entity.feedbackContent.ErrorTypeMap;
import com.mocah.mindmath.server.repository.LocalRoute;
import com.mocah.mindmath.server.repository.LocalRouteRepository;

@RestController
@RequestMapping("/learning")
public class Learningcontroller {

	@Autowired
	private RedisTemplate<String, Serializable> serializableRedisTemplate;

	private static final String license_num = "mocah";

	/**
	 * check the post request based on authorization
	 *
	 * @param auth the authorization parameter from headers
	 * @return authorized or unauthorized
	 */
	private static boolean checkauth(String auth) {
		if (auth.equalsIgnoreCase(license_num))
			return true;
		return false;
	}
	
	/**
	 * update decision tree and initialize decision tree
	 * @param auth
	 * @param data if not null, the json of decision tree
	 * @return
	 * @throws JsonParserCustomException
	 * @throws IOException
	 */
	@PostMapping(path = "/init/decisiontree", consumes = "application/json")
	public ResponseEntity<String> initDecisionTree(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws IOException{
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		String message = "";
		if(!data.equalsIgnoreCase("null"))
		{
			LocalRouteRepository.writeFile(data, LocalRoute.DecisionTreeRoute);
			message += "Decision Tree updated.";
		}
		
		LearningProcess.initLearningProcess();
		return new ResponseEntity<String>("QLearning initialized. " + message, HttpStatus.OK);
	}
	/**
	 * initialize expert learning
	 * @param auth
	 * @param id_task the key name of values in expert learning in Redis
	 * @return
	 * @throws JsonParserCustomException
	 * @throws IOException
	 */
	@PostMapping(path = "/init/expertlearning", consumes = "application/json")
	public ResponseEntity<String> initExpertLearning(@RequestHeader("Authorization") String auth,
			@RequestParam String id_task) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");
		serializableRedisTemplate.opsForValue().set(id_task, null);
		return new ResponseEntity<String>("Expert learning of id_task \"" + id_task +"\" initialized.", HttpStatus.OK);
	}
	
	/**
	 * a test to connect with Redis
	 * @param auth
	 * @param data
	 * @return
	 * @throws JsonParserCustomException
	 * @throws IOException
	 */
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
	 * Get actual qValues from Qlearning
	 *
	 * @param auth
	 * @return the qvalue from Qlearning algorithm
	 */
	@GetMapping("/qlearning/qvalues")
	public ResponseEntity<String> getQlearningQValues(@RequestHeader("Authorization") String auth) {
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
	 * Get actual qValues from expert learning
	 *
	 * @param auth
	 * @return the qvalue from expert learning algorithm
	 */
	@GetMapping("/expertlearning/qvalues")
	public ResponseEntity<String> getExpertlearningQValues(@RequestHeader("Authorization") String auth) {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ILearning learning = LearningProcess.getExpertlearning();
		Map<IState, LinkedList<IValue>> qValues = null;

		if (learning instanceof ExpertLearning) {
			ExpertLearning ql = (ExpertLearning) learning;
			qValues = ql.getqValues();
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
	 * update code error
	 * @param auth
	 * @param data
	 * @return
	 * @throws JsonParserCustomException
	 */
	@PostMapping(path = "/errorcode", consumes = "application/json")
	public ResponseEntity<String> updateErrorCode(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ErrorTypeMap.setError(data);
		return new ResponseEntity<String>(ErrorTypeMap.toStringGson(), HttpStatus.OK);
	}
	
	/**
	 * initialize code error
	 * @param auth
	 * @param data
	 * @return
	 * @throws JsonParserCustomException
	 */
	@DeleteMapping(path = "/errorcode", consumes = "application/json")
	public ResponseEntity<String> initErrorCode(@RequestHeader("Authorization") String auth,
			@RequestBody String data) throws JsonParserCustomException {
		if (!checkauth(auth))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized connection.");

		ErrorTypeMap.init();
		return new ResponseEntity<String>(ErrorTypeMap.toStringGson(), HttpStatus.OK);
	}
}

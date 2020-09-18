package com.mocah.mindmath.server.controller.learning;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mocah.mindmath.learning.LearningProcess;
import com.mocah.mindmath.learning.algorithms.ILearning;
import com.mocah.mindmath.learning.algorithms.QLearning;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;

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
		if (auth.equals(license_num))
			return true;
		return false;
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
}

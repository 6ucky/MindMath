package com.mocah.mindmath.repository.learninglocker;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.repository.XAPIRepository;
import com.mocah.mindmath.server.entity.task.Log;
import com.mocah.mindmath.server.entity.task.Sensors;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.IStatementObject;
import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.adapters.ActorAdapter;
import gov.adlnet.xapi.model.adapters.StatementObjectAdapter;

/**
 * Use Spring Rest Template to connect to Learning Locker
 *
 * @author Yan Wang
 * @since 09/03/2020
 */
@Service
public class LearningLockerRepositoryAggregation extends LearningLockerRepository implements XAPIRepository {
	private HashMap<String, String> pipeline = null;
	private Gson gson = new Gson();

	public LearningLockerRepositoryAggregation() {
		this(new FeedbackforLRS(), false);
	}

	public LearningLockerRepositoryAggregation(boolean isTestEnv) {
		this(new FeedbackforLRS(), isTestEnv);
	}

	public LearningLockerRepositoryAggregation(FeedbackforLRS fbLRS) {
		this(fbLRS, false);
	}

	public LearningLockerRepositoryAggregation(FeedbackforLRS fbLRS, boolean isTestEnv) {
		super(fbLRS, isTestEnv);
	}

	@Override
	protected HttpHeaders InitializeHeader() {
		HttpHeaders headers = new HttpHeaders();

		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	@Override
	protected HttpHeaders InitializeTestHeader() {
		HttpHeaders headers = new HttpHeaders();

		// header as chart form
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add(BASIC_AUTHORIZATION, BASIC_AUTHORIZATION_TEST_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return headers;
	}

	/**
	 * Add quary parameters in URL
	 *
	 * @return
	 */
	private StringBuilder InitializeQueryURL() {

		// https://lrsmocah.lip6.fr/api/statements/aggregate?cache=false&maxTimeMS=5000&maxScan=10000&pipeline=%5B%7B%22%24match%22%3A%7B%22%24and%22%3A%5B%7B%22%24or%22%3A%5B%7B%22person%22%3Anull%7D%5D%2C%22%24comment%22%3A%22%7B%5C%22criterionLabel%5C%22%3A%5C%22A%5C%22%2C%5C%22criteriaPath%5C%22%3A%5B%5C%22person%5C%22%5D%7D%22%7D%2C%7B%22%24comment%22%3A%22%7B%5C%22criterionLabel%5C%22%3A%5C%22B%5C%22%2C%5C%22criteriaPath%5C%22%3A%5B%5C%22lrs_id%5C%22%5D%7D%22%2C%22%24or%22%3A%5B%7B%22lrs_id%22%3A%7B%22%24oid%22%3A%225ee3440719a0867c2982f6f9%22%7D%7D%5D%7D%2C%7B%22%24comment%22%3A%22%7B%5C%22criterionLabel%5C%22%3A%5C%22C%5C%22%2C%5C%22criteriaPath%5C%22%3A%5B%5C%22statement%5C%22%2C%5C%22object%5C%22%2C%5C%22definition%5C%22%2C%5C%22extensions%5C%22%2C%5C%22https%3A%2F%2Fwww%2646%3Blip6%2646%3Bfr%2Fmocah%2FinvalidURI%2Fextensions%2Finput-context%5C%22%5D%7D%22%2C%22%24or%22%3A%5B%7B%22statement.object.definition.extensions.https%3A%2F%2Fwww%2646%3Blip6%2646%3Bfr%2Fmocah%2FinvalidURI%2Fextensions%2Finput-context%22%3A%22user-input%22%7D%5D%7D%5D%7D%7D%5D
		StringBuilder query = new StringBuilder();
		query.append("?");
		query.append("cache=false");
		query.append("&");
		query.append("maxTimeMS=5000");
		query.append("&");
		query.append("maxScan=10000");
		query.append("&");

		StringBuilder pipeline = new StringBuilder();
		if (this.pipeline != null && !this.pipeline.isEmpty()) {

			for (Entry<String, String> item : this.pipeline.entrySet()) {
				pipeline.append("{\"");
				pipeline.append(item.getKey());
				pipeline.append("\"");
				pipeline.append(":");
				pipeline.append(item.getValue());
				pipeline.append("},");
			}

			pipeline.deleteCharAt(query.length() - 1);
		} else {
			pipeline.append("[]");
		}

		try {
			String pipelineEncoded = URLEncoder.encode(pipeline.toString(), "UTF-8");
			query.append("pipeline=");
			query.append(pipelineEncoded);
		} catch (UnsupportedEncodingException e) {
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}

		return query;
	}

	/**
	 * Add Generic Interface Adapter of Actor class and IStatementObject class for
	 * Gson
	 *
	 * @return
	 */
	@Override
	protected Gson getDecoder() {
		Gson gson_decoder = new Gson();
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Actor.class, new ActorAdapter());
		builder.registerTypeAdapter(IStatementObject.class, new StatementObjectAdapter());
		gson_decoder = builder.create();

		return gson_decoder;
	}

	/**
	 * get about of LRS
	 *
	 * @return message from Learning Locker
	 */
	@Override
	public String getAboutfromLearningLocker() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);
		ResponseEntity<String> response = this.restTemp.exchange(ABOUT_URL, HttpMethod.GET, entity, String.class);
		return response.getBody();
	}

	@Override
	public String getAllStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_AGGREGATION_URL, HttpMethod.GET, entity,
				String.class);
		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getAllStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_AGGREGATION_URL, HttpMethod.GET, entity,
				String.class);
		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

	@Override
	public String getFilteredStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(
				STATEMENT_AGGREGATION_URL + InitializeQueryURL().toString(), HttpMethod.GET, entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getFilteredStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// set true to URI string
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(STATEMENT_AGGREGATION_URL + InitializeQueryURL().toString());
		URI uri = builder.build(true).toUri();
		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

	public String postStatementTEST(String id, Sensors sensors, List<Log> log) {
		// TODO
		return null;
	}

	/**
	 * Add a stage to the pipeline's statement request
	 *
	 * @param key   key name (ie "context.extensions.yourExtension")
	 * @param value expected value (ie "customExtensionValue")
	 */
	public LearningLockerRepositoryAggregation addPipelineStage(String key, String value) {
		LearningLockerRepositoryAggregation client = new LearningLockerRepositoryAggregation(this.fbLRS,
				this.isTestEnv);
		if (client.pipeline == null) {
			client.pipeline = new HashMap<>();
		}
		if (this.pipeline != null) {
			for (Entry<String, String> filter : pipeline.entrySet()) {
				client.pipeline.put(filter.getKey(), filter.getValue());
			}
		}
		client.pipeline.put(key, value);

		return client;
	}

	/**
	 *
	 */
	public void clearFilters() {
		this.pipeline.clear();
	}

	/**
	 * @param matchQuery
	 */
	public LearningLockerRepositoryAggregation filterByActivity(String matchQuery) {
		return addPipelineStage("$match", matchQuery);
	}

	@Override
	public String postStatement(Statement statement) {
		// TODO
		return null;
	}
}

package com.mocah.mindmath.server.repository.learninglocker;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.server.repository.XAPIRepository;

import gov.adlnet.xapi.model.Statement;
import gov.adlnet.xapi.model.StatementResult;

/**
 * Use Spring Rest Template to connect to Learning Locker
 *
 * @author Yan Wang
 * @since 09/03/2020
 */
@Service
public class LearningLockerRepositoryAggregation extends LearningLockerRepository implements XAPIRepository {
	private HashMap<String, String> pipeline = null;

	public LearningLockerRepositoryAggregation() {
		this(false);
	}

	public LearningLockerRepositoryAggregation(boolean isTestEnv) {
		super(isTestEnv);
	}

	/**
	 * Add quary parameters in URL
	 *
	 * @return
	 */
	private StringBuilder InitializeQueryURL() {
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
			pipeline.append("[");
			for (Entry<String, String> item : this.pipeline.entrySet()) {
				pipeline.append("{\"");
				pipeline.append(item.getKey());
				pipeline.append("\"");
				pipeline.append(":");
				pipeline.append(item.getValue());
				pipeline.append("},");
			}

			pipeline.deleteCharAt(pipeline.length() - 1);
			pipeline.append("]");
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

	@Override
	public String getAllStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// Remove pipeline filter
		HashMap<String, String> old = this.pipeline;
		this.pipeline = null;

		URI uri = getURI();

		// Restore pipeline filter
		this.pipeline = old;

		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getAllStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// Remove pipeline filter
		HashMap<String, String> old = this.pipeline;
		this.pipeline = null;

		URI uri = getURI();

		// Restore pipeline filter
		this.pipeline = old;

		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		return aggregationResultToStatementResult(response.getBody());
	}

	@Override
	public String getFilteredStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		URI uri = getURI();

		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getFilteredStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		URI uri = getURI();

		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		return aggregationResultToStatementResult(response.getBody());
	}

	/**
	 * Generate an URI and set true to URI string
	 *
	 * @return
	 */
	private URI getURI() {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(STATEMENT_AGGREGATION_URL + InitializeQueryURL().toString());
		System.out.println(STATEMENT_AGGREGATION_URL + InitializeQueryURL().toString());
		URI uri = builder.build(true).toUri();

		return uri;
	}

	private StatementResult aggregationResultToStatementResult(String body) {
		StatementResult sr = new StatementResult();
		ArrayList<Statement> statements = new ArrayList<>();

		Gson decoder = getDecoder();

		JsonArray results = gson.fromJson(body, JsonArray.class);
		for (JsonElement result : results) {
			JsonObject one_result = result.getAsJsonObject();

			Statement statement = decoder.fromJson(one_result.get("statement"), Statement.class);
			statements.add(statement);
		}

		sr.setStatements(statements);

		return sr;
	}

	/**
	 * Add a stage to the pipeline's statement request
	 *
	 * @param key   key name (ie "context.extensions.yourExtension")
	 * @param value expected value (ie "customExtensionValue")
	 */
	public LearningLockerRepositoryAggregation addPipelineStage(String key, String value) {
		LearningLockerRepositoryAggregation client = new LearningLockerRepositoryAggregation(this.isTestEnv);
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
	public void clearPipelineStages() {
		this.pipeline.clear();
	}

	/**
	 * @param matchQuery
	 * @return
	 */
	public LearningLockerRepositoryAggregation filterByMatcher(String matchQuery) {
		return addPipelineStage("$match", matchQuery);
	}

	/**
	 * @param limit
	 * @return
	 */
	public LearningLockerRepositoryAggregation limitResults(Integer limit) {
		if (limit > 0)
			return addPipelineStage("$limit", limit + "");

		return this;
	}
}

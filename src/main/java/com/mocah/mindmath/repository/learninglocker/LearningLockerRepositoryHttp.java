package com.mocah.mindmath.repository.learninglocker;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.mocah.mindmath.parser.jsonparser.JsonParserLRS;
import com.mocah.mindmath.parser.jsonparser.LRSType;
import com.mocah.mindmath.repository.XAPIRepository;

import gov.adlnet.xapi.model.Actor;
import gov.adlnet.xapi.model.StatementResult;
import gov.adlnet.xapi.model.Verb;

/**
 * Use Spring Rest Template to connect to Learning Locker
 *
 * @author Yan Wang
 * @since 09/03/2020
 */
@Service
public class LearningLockerRepositoryHttp extends LearningLockerRepository implements XAPIRepository {
	private HashMap<String, String> filters = null;

	public LearningLockerRepositoryHttp() {
		this(false);
	}

	public LearningLockerRepositoryHttp(boolean isTestEnv) {
		super(isTestEnv);
	}

	/**
	 * Add quary parameters in URL
	 *
	 * @return
	 */
	private StringBuilder InitializeQueryURL() {
		StringBuilder query = new StringBuilder();
		if (this.filters != null && !this.filters.isEmpty()) {
			query.append("?");
			for (Entry<String, String> item : this.filters.entrySet()) {
				query.append(item.getKey());
				query.append("=");
				query.append(item.getValue());
				query.append("&");
			}
			query.deleteCharAt(query.length() - 1);
		}
		return query;
	}

	@Override
	public String getAllStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);
		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getAllStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		ResponseEntity<String> response = this.restTemp.exchange(STATEMENT_URL, HttpMethod.GET, entity, String.class);

		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

	@Override
	public String getFilteredStatementsAsString() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// set true to URI string
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(STATEMENT_URL + InitializeQueryURL().toString());
		URI uri = builder.build(true).toUri();
		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		JsonParserLRS parser = new JsonParserLRS(response.getBody(), LRSType.RESPONSE);

		return parser.getStatement();
	}

	@Override
	public StatementResult getFilteredStatements() {
		HttpEntity<String> entity = new HttpEntity<>(header_entity);

		// set true to URI string
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl(STATEMENT_URL + InitializeQueryURL().toString());
		URI uri = builder.build(true).toUri();
		ResponseEntity<String> response = this.restTemp.exchange(uri, HttpMethod.GET, entity, String.class);

		return getDecoder().fromJson(response.getBody(), StatementResult.class);
	}

	/**
	 * Add a filter to the statement request
	 *
	 * @param key   key name (ie "context.extensions.yourExtension")
	 * @param value expected value (ie "customExtensionValue")
	 */
	public LearningLockerRepositoryHttp addFilter(String key, String value) {
		LearningLockerRepositoryHttp client = new LearningLockerRepositoryHttp(this.isTestEnv);
		if (client.filters == null) {
			client.filters = new HashMap<>();
		}
		if (this.filters != null) {
			for (Entry<String, String> filter : filters.entrySet()) {
				client.filters.put(filter.getKey(), filter.getValue());
			}
		}
		client.filters.put(key, value);
		return client;
	}

	/**
	 *
	 */
	public void clearFilters() {
		this.filters.clear();
	}

	/**
	 * @param v
	 * @throws UnsupportedEncodingException
	 */
	public LearningLockerRepositoryHttp filterByVerb(Verb v) throws UnsupportedEncodingException {
		return addFilter("verb", URLEncoder.encode(v.getId(), "UTF-8"));
	}

	/**
	 * @param verbId
	 */
	public LearningLockerRepositoryHttp filterByVerb(String verbId) {
		return addFilter("verb", verbId);
	}

	/**
	 * @param a
	 * @throws UnsupportedEncodingException
	 */
	public LearningLockerRepositoryHttp filterByActor(Actor a) throws UnsupportedEncodingException {
		return addFilter("agent", URLEncoder.encode(gson.toJson(a.serialize()), "UTF-8"));
	}

	/**
	 * @param activityId
	 */
	public LearningLockerRepositoryHttp filterByActivity(String activityId) {
		return addFilter("activity", activityId);
	}

	/**
	 * @param registrationId
	 */
	public LearningLockerRepositoryHttp filterByRegistration(String registrationId) {
		return addFilter("registration", registrationId);
	}

	/**
	 * @param include
	 */
	public LearningLockerRepositoryHttp includeRelatedActivities(boolean include) {
		if (include)
			return addFilter("related_activities", "true");
		else
			return addFilter("related_activities", "false");
	}

	/**
	 * @param include
	 */
	public LearningLockerRepositoryHttp includeRelatedAgents(boolean include) {
		if (include)
			return addFilter("related_agents", "true");
		else
			return addFilter("related_agents", "false");
	}

	/**
	 * @param timestamp
	 */
	public LearningLockerRepositoryHttp filterBySince(String timestamp) {
		return addFilter("since", timestamp);
	}

	/**
	 * @param timestamp
	 */
	public LearningLockerRepositoryHttp filterByUntil(String timestamp) {
		return addFilter("until", timestamp);
	}

	/**
	 * @param limit
	 */
	public LearningLockerRepositoryHttp limitResults(int limit) {
		return addFilter("limit", Integer.toString(limit));
	}

	/**
	 *
	 */
	public LearningLockerRepositoryHttp exact() {
		return addFilter("format", "exact");
	}

	/**
	 *
	 */
	public LearningLockerRepositoryHttp ids() {
		return addFilter("format", "ids");
	}

	/**
	 *
	 */
	public LearningLockerRepositoryHttp canonical() {
		return addFilter("format", "canonical");
	}

	/**
	 * @param include
	 */
	public LearningLockerRepositoryHttp ascending(boolean include) {
		if (include)
			return addFilter("ascending", "true");
		else
			return addFilter("ascending", "false");
	}
}

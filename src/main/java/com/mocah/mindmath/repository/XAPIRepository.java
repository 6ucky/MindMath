package com.mocah.mindmath.repository;

import java.util.List;

import com.mocah.mindmath.repository.learninglocker.jxapi.Statement;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

public interface XAPIRepository {

	public String getAboutfromLearningLocker();

	/**
	 * Get all the statements from LRS in json format
	 *
	 * @return list of statements as json string
	 */
	public String getAllStatementsfromLearningLockerAsString();

	/**
	 * Get all the statements from LRS
	 *
	 * @return list of statements as list of Statement object
	 */
	public List<Statement> getAllStatementsfromLearningLocker();

	/**
	 * Get statements from LRS in json format (with filter, if no filter then is
	 * equivalent to {@code getAllStatementsfromLearningLockerAsString})
	 *
	 * @return list of statements as json string
	 */
	public String getFilteredStatementsAsString();

	/**
	 * Get statements from LRS (with filter, if no filter then is equivalent to
	 * {@code getAllStatementsfromLearningLockerAsString})
	 *
	 * @return list of statements as list of Statement object
	 */
	public List<Statement> getFilteredStatements();

	public String postStatementTEST(String id, Sensors sensors, List<Log> log);
}

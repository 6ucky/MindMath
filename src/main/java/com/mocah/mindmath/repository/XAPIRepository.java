package com.mocah.mindmath.repository;

import com.mocah.mindmath.repository.jxapi.Statement;
import com.mocah.mindmath.repository.jxapi.StatementResult;

public interface XAPIRepository {

	public String getAboutfromLearningLocker();

	/**
	 * Get all the statements from LRS in json format
	 *
	 * @return list of statements as json string
	 */
	public String getAllStatementsAsString();

	/**
	 * Get all the statements from LRS
	 *
	 * @return list of statements as list of Statement object
	 */
	public StatementResult getAllStatements();

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
	public StatementResult getFilteredStatements();

	/**
	 * POST statement to LRS
	 * @param statement
	 * @return the id of statement
	 */
	public String postStatement(Statement statement);
}

/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IProfile {
	/**
	 * Generate a new learner ID.
	 *
	 */
	public void generateLearnerID();

	/**
	 * Initialize the profile.<br>
	 * Must generate a learner ID with {@code generateLearnerID()}, if it isn't
	 * already set.
	 *
	 */
	public void initLearner();

	/**
	 * Get the learner ID.<br>
	 * <br>
	 * <b>NOTE:</b> Call {@code initLearner()} if the learner ID isn't already set.
	 *
	 * @return a learner ID
	 */
	public String getLearnerID();

	/**
	 * @param fdbkInfoWeight
	 */
	public void calcNewSuccessProb(int fdbkInfoWeight);

	/**
	 *
	 */
	public void learnFromExercise();
}

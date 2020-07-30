/**
 *
 */
package com.mocah.mindmath.datasimulation;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class FeedbackData {
	private String idLearner;
	private String idFbCabri;
	private String idFamilytask;
	private String idFeedback;
	private String motivationalElementFb;
	private String contentFb;
	private String glossaryFb;
	private String mode;
	private Double reward;

	/**
	 * @return the feedback id
	 */
	public String getIdFeedback() {
		return idFeedback;
	}

	/**
	 * @return the reward
	 */
	public Double getReward() {
		return reward;
	}
}

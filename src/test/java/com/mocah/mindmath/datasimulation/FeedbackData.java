/**
 *
 */
package com.mocah.mindmath.datasimulation;

import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class FeedbackData {
	@Expose
	private String idLearner;
	@Expose
	private String idTask;
	@Expose
	private String idFbCabri;
	@Expose
	private String idFamilytask;
	@Expose
	private String idFb;
	@Expose
	private String motivationalElementFb;
	@Expose
	private String contentFb;
	@Expose
	private String glossaryFb;
	@Expose
	private String correctAnswer;
	@Expose
	private String successScore;
	@Expose
	private String closeTask;
	@Expose
	private String mode;
	@Expose
	private Double reward;
	@Expose
	private String modifiedState;
	@Expose
	private Map<String, Double> qvalues;
	// TODO feedback suite à exploration ou exploitation ?

	/**
	 * @return the feedback id
	 */
	public String getIdFeedback() {
		return idFb;
	}

	/**
	 * @return the reward
	 */
	public Double getReward() {
		return reward;
	}

	/**
	 * @return the modified state
	 */
	public String getModifiedState() {
		return modifiedState;
	}

	/**
	 * @return the qvalues for this state
	 */
	public Map<String, Double> getQvalues() {
		return qvalues;
	}
}

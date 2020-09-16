package com.mocah.mindmath.datasimulation.json;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.mocah.mindmath.datasimulation.CabriData;
import com.mocah.mindmath.datasimulation.FeedbackData;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class SimulatedData {
	@Expose
	private int iteration;
	@Expose
	private double exerciseSuccessProb;
	@Expose
	private double activityModeIncreaseSuccessProb;
	@Expose
	private CabriData generated;
	@Expose
	private FeedbackData feedback;
	@Expose
	private Date date;

	public SimulatedData(int it) {
		this(it, null, null);
	}

	public SimulatedData(int it, CabriData generated) {
		this(it, generated, null);
	}

	public SimulatedData(int it, CabriData generated, FeedbackData feedback) {
		this.iteration = it;
		this.generated = generated;
		this.feedback = feedback;
		this.date = new Date();
	}

	/**
	 * @return the iteration
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * @return the learner's exercise success probability
	 */
	public double getExerciseSuccessProb() {
		return this.exerciseSuccessProb;
	}

	/**
	 * @param exerciseSuccessProb the learner's exercise success probability to
	 *                            define
	 */
	public void setExerciseSuccessProb(double exerciseSuccessProb) {
		this.exerciseSuccessProb = exerciseSuccessProb;
	}

	/**
	 * @return the learner's Activity Mode increase success probability
	 */
	public double getActivityModeIncreaseSuccessProb() {
		return this.activityModeIncreaseSuccessProb;
	}

	/**
	 * @param activityModeIncreaseSuccessProb the learner's Activity Mode increase
	 *                                        success probability to define
	 */
	public void setActivityModeIncreaseSuccessProb(double activityModeIncreaseSuccessProb) {
		this.activityModeIncreaseSuccessProb = activityModeIncreaseSuccessProb;
	}

	/**
	 * @return the generated data
	 */
	public CabriData getGenerated() {
		return generated;
	}

	/**
	 * @param generated the generated data to define
	 */
	public void setGenerated(CabriData generated) {
		this.generated = generated;
	}

	/**
	 * @return the feedback received
	 */
	public FeedbackData getFeedback() {
		return feedback;
	}

	/**
	 * @param feedback the feedback to define
	 */
	public void setFeedback(FeedbackData feedback) {
		this.feedback = feedback;
	}

	/**
	 * @return the generation date
	 */
	public Date getDate() {
		return date;
	}
}

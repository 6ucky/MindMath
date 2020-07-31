/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mocah.mindmath.datasimulation.AppConfig;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.GeneratorEnum;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class AbstractProfile implements IProfile {
	private boolean learnerInited;
	private String learnerID;

	protected DomainEnum domain;
	protected GeneratorEnum generator;
	protected TaskFamilyEnum taskFamily;
	protected ActivityModeEnum activityMode;

	/**
	 * Does the learner car about informations in feedbacks ?
	 */
	protected boolean readingFeedback;
	/**
	 * Does the learner wants only informative feedbacks ?
	 */
	@Deprecated
	protected boolean waitingInformative;
	/**
	 * Minimal weight for a feedback considered as "informative"
	 */
	@Deprecated
	protected int minWheightInfo;

	protected double baseSuccessProb;
	protected double successProb;

	/**
	 * Map of modification deltas for each feedback info weight.<br>
	 * <br>
	 * Example:
	 * {@code successProb = successProb + successProb * DELTAS.getOrDefault(fdbkInfoWeight, defaultDelta)}
	 */
	protected Map<Integer, Double> deltas;
	/**
	 * Default delta to use when there is no corresponding fdbkInfoWeight
	 */
	protected double defaultDelta;
	protected boolean percentMode;
	/**
	 * When {@code successProb = 0}, calc of new prob will use
	 * {@code firstIncreaseProb} instead.<br>
	 * <br>
	 * Example:
	 * {@code prob = firstIncreaseProb * deltas.getOrDefault(fdbkInfoWeight, defaultDelta);}
	 */
	protected double firstIncreaseProb;
	/**
	 * Part of success prob obtained conserved at the end of each exercise.<br>
	 * Will be reset {@code baseSuccessProb} to
	 * {@code baseSuccessProb + successProb * exerciseDelta}
	 */
	protected double exerciseDelta;

	protected double askHelpProb = 0;

	/**
	 *
	 */
	public AbstractProfile() {
		this.learnerInited = false;
	}

	/**
	 * @param learnerID
	 */
	public AbstractProfile(String learnerID) {
		this.learnerID = learnerID;
		this.learnerInited = true;
	}

	@Override
	public void generateLearnerID() {
		Date date = new Date();
		this.learnerID = AppConfig.learnerBase + date.getTime();
		this.learnerInited = true;
	}

	@Override
	public void initLearner() {
		if (!this.learnerInited) {
			generateLearnerID();
		}

		this.domain = DomainEnum.ALGEBRA;
		this.generator = GeneratorEnum.RES_EQ_PERMIER_DEGRE;
		this.taskFamily = TaskFamilyEnum.FT3_2_1;
		this.activityMode = ActivityModeEnum.A0;

		// Feedback consideration
		this.readingFeedback = false;
		this.waitingInformative = false;
		this.minWheightInfo = 0;

		// Exercise Success
		this.baseSuccessProb = 0.5;
		this.successProb = this.baseSuccessProb;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.01, 1, 0.02, 2, 0.04, 3, 0.08, 4, 0.16));
		this.defaultDelta = 0.1;
		this.firstIncreaseProb = 0.0;
		this.percentMode = true;
		this.exerciseDelta = 0.0;

		this.askHelpProb = 0.0;
	}

	@Override
	public String getLearnerID() {
		return this.learnerID;
	}

	/**
	 * @return the domain
	 */
	public DomainEnum getDomain() {
		return domain;
	}

	/**
	 * @return the generator
	 */
	public GeneratorEnum getGenerator() {
		return generator;
	}

	/**
	 * @return the taskFamily
	 */
	public TaskFamilyEnum getTaskFamily() {
		return taskFamily;
	}

	/**
	 * @return the activityMode
	 */
	public ActivityModeEnum getActivityMode() {
		return activityMode;
	}

	/**
	 * @return the readingFeedback
	 */
	public boolean isReadingFeedback() {
		return readingFeedback;
	}

	/**
	 * @return the waitingInformative
	 */
	@Deprecated
	public boolean isWaitingInformative() {
		return waitingInformative;
	}

	/**
	 * @return the minWheightInfo
	 */
	@Deprecated
	public int getMinWheightInfo() {
		return minWheightInfo;
	}

	/**
	 * @return the successProb
	 */
	public double getSuccessProb() {
		return successProb;
	}

	@Override
	public void calcNewSuccessProb(int fdbkInfoWeight) {
		double prob = 0.0;
		if (this.percentMode) {
			if (successProb == 0 && firstIncreaseProb > 0) {
				prob = firstIncreaseProb * deltas.getOrDefault(fdbkInfoWeight, defaultDelta);
			} else {
				prob = successProb + successProb * deltas.getOrDefault(fdbkInfoWeight, defaultDelta);
			}
		} else {
			prob = successProb + deltas.getOrDefault(fdbkInfoWeight, defaultDelta);
		}

		if (prob > 1) {
			prob = 1;
		} else if (prob < 0) {
			prob = 0;
		}

		this.successProb = prob;
	}

	@Override
	public void learnFromExercise() {
		double prob = baseSuccessProb + successProb * exerciseDelta;

		if (prob > 1) {
			prob = 1;
		} else if (prob < 0) {
			prob = 0;
		}

		this.baseSuccessProb = prob;
		this.successProb = this.baseSuccessProb;
	}

	/**
	 * @return the askHelpProb
	 */
	public double getAskHelpProb() {
		return askHelpProb;
	}
}

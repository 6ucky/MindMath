/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
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
	@Expose
	protected String profileName;

	private boolean learnerInited;
	private String learnerID;

	protected DomainEnum domain;
	protected GeneratorEnum generator;
	protected TaskFamilyEnum taskFamily;
	protected ActivityModeEnum activityMode;
	protected Map<ActivityModeEnum, Double> initialActivityModeProb;
	protected Map<ActivityModeEnum, Double> deltaActivityModeProb;
	protected double activityModeIncreaseProb;

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
		this.profileName = this.getClass().getName();
	}

	/**
	 * @param learnerID
	 */
	public AbstractProfile(String learnerID) {
		this();
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

		this.initialActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, (double) 1 / 3, ActivityModeEnum.A1,
				(double) 1 / 3, ActivityModeEnum.A2, (double) 1 / 3);
		this.activityMode = initActivityMode();
		this.deltaActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, 0.0, ActivityModeEnum.A1, 0.0,
				ActivityModeEnum.A2, 0.0);
		this.activityModeIncreaseProb = this.deltaActivityModeProb.get(activityMode);

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

	protected ActivityModeEnum initActivityMode() {
		List<ActivityModeEnum> activityModes = new ArrayList<>(this.initialActivityModeProb.keySet());

		double[] cumprob = new double[this.initialActivityModeProb.size() + 1];
		cumprob[0] = 0.0;
		double total = 0.0;
		for (int i = 0; i < activityModes.size(); i++) {
			total += this.initialActivityModeProb.get(activityModes.get(i));
			cumprob[i + 1] = total;
		}

		ActivityModeEnum res = ActivityModeEnum.A0;

		double d = cumprob[0] + Math.random() * (total - cumprob[0]);
		for (int i = 0; i < activityModes.size(); i++) {
			if (d > cumprob[i] && d <= cumprob[i + 1]) {
				res = activityModes.get(i);
				break;
			}
		}

		return res;
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

	/**
	 * @return the probability for the learner to see it's activity mode beeing
	 *         inceased (at the end of an exercise)
	 */
	public double getActivityModeIncreaseProb() {
		return this.activityModeIncreaseProb;
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

		// Increase activity mode
		Random rand = new Random();
		double d = rand.nextDouble();
		if (d < this.activityModeIncreaseProb) {
			ActivityModeEnum newActivityMode = ActivityModeEnum.valueOf(this.activityMode.getValue() + 1);

			if (newActivityMode != null) {
				this.activityMode = newActivityMode;
				this.activityModeIncreaseProb = this.deltaActivityModeProb.get(this.activityMode);
			}
		} else {
			this.activityModeIncreaseProb = this.activityModeIncreaseProb
					+ this.activityModeIncreaseProb * this.deltaActivityModeProb.get(this.activityMode);
		}
	}

	/**
	 * @return the askHelpProb
	 */
	public double getAskHelpProb() {
		return askHelpProb;
	}
}

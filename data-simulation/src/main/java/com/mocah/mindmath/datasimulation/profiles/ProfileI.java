/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ProfileI extends AbstractProfile {

	/**
	 *
	 */
	public ProfileI() {
	}

	/**
	 * @param learnerID
	 */
	public ProfileI(String learnerID) {
		super(learnerID);
	}

	@Override
	public void initLearner() {
		super.initLearner();

		// Feedback consideration
		this.readingFeedback = false;

		this.initialActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, (double) 1 / 5, ActivityModeEnum.A1,
				(double) 2 / 5, ActivityModeEnum.A2, (double) 2 / 5);
		this.activityMode = initActivityMode();
		this.deltaActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, 0.30, ActivityModeEnum.A1, 0.20,
				ActivityModeEnum.A2, 0.0);
		this.activityModeIncreaseProb = this.deltaActivityModeProb.get(activityMode);

		// Exercise Success
		this.baseSuccessProb = 0.6;
		this.successProb = this.baseSuccessProb;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.0, 1, 0.0, 2, 0.0, 3, 0.0, 4, 0.0));
		this.defaultDelta = 0.01;

		this.askHelpProb = 0.0;
	}
}

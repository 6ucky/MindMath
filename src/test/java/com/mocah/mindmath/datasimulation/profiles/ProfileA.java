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
public class ProfileA extends AbstractProfile {

	/**
	 *
	 */
	public ProfileA() {
	}

	/**
	 * @param learnerID
	 */
	public ProfileA(String learnerID) {
		super(learnerID);
	}

	@Override
	public void initLearner() {
		super.initLearner();

		// Feedback consideration
		this.readingFeedback = true;
//		this.waitingInformative = true;
//		this.minWheightInfo = 3;

		this.initialActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, (double) 2 / 5, ActivityModeEnum.A1,
				(double) 2 / 5, ActivityModeEnum.A2, (double) 1 / 5);
		this.activityMode = initActivityMode();
		this.deltaActivityModeProb = ImmutableMap.of(ActivityModeEnum.A0, 0.0, ActivityModeEnum.A1, 0.0,
				ActivityModeEnum.A2, 0.0);
		this.activityModeIncreaseProb = this.deltaActivityModeProb.get(activityMode);

		// Exercise Success
		this.baseSuccessProb = 0.0;
		this.successProb = this.baseSuccessProb;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.0, 1, 0.0, 2, 0.0, 3, 0.5, 4, 1.0));
		this.firstIncreaseProb = 1.0;
		this.defaultDelta = 0.0;

		this.askHelpProb = 0.0;
	}
}

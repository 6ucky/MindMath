/**
 *
 */
package com.mocah.mindmath.datasimulation.profiles;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ProfileJ extends AbstractProfile {

	/**
	 *
	 */
	public ProfileJ() {
	}

	/**
	 * @param learnerID
	 */
	public ProfileJ(String learnerID) {
		super(learnerID);
	}

	@Override
	public void initLearner() {
		super.initLearner();

		// Feedback consideration
		this.readingFeedback = true;
		this.waitingInformative = true;
		this.minWheightInfo = 3;

		// Exercise Success
		this.baseSuccessProb = 0.0;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.01, 1, 0.01, 2, 0.01, 3, 0.75, 4, 1.0));
		this.firstIncreaseProb = 1.0;
		this.defaultDelta = 0.0;

		this.askHelpProb = 0.0;
	}
}

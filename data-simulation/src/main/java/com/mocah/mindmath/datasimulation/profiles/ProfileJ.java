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
		this.readingFeedback = false;

		// Exercise Success
		this.baseSuccessProb = 0.0;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.025, 1, 0.05, 2, 0.10, 3, 0.20, 4, 0.40));
		this.firstIncreaseProb = 1.0;
		this.defaultDelta = 0.02;

		this.askHelpProb = 0.0;
	}
}

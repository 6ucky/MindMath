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
public class ProfileB extends AbstractProfile {

	/**
	 *
	 */
	public ProfileB() {
	}

	/**
	 * @param learnerID
	 */
	public ProfileB(String learnerID) {
		super(learnerID);
	}

	@Override
	public void initLearner() {
		super.initLearner();

		// Feedback consideration
		this.readingFeedback = true;

		// Exercise Success
		this.baseSuccessProb = 0.5;
		this.successProb = this.baseSuccessProb;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.6, 1, 0.7, 2, 0.8, 3, 0.9, 4, 1.0));
		this.firstIncreaseProb = 0.0;
		this.defaultDelta = 0.1;

		this.askHelpProb = 0.0;
	}
}

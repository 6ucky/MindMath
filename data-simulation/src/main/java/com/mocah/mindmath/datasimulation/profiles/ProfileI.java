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

		// Exercise Success
		this.baseSuccessProb = 0.9;
		this.successProb = this.baseSuccessProb;
		this.deltas = Maps.newHashMap(ImmutableMap.of(0, 0.0, 1, 0.0, 2, 0.0, 3, 0.0, 4, 0.0));
		this.defaultDelta = 0.0;

		this.askHelpProb = 0.0;
	}
}

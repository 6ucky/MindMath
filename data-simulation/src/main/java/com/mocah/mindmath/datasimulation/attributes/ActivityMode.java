/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.ActivityModeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ActivityMode extends Attribute<Integer> {

	/**
	 *
	 */
	public ActivityMode(ActivityModeEnum val) {
		this.value = val;
	}
}

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.TriggerEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Trigger extends Attribute<TriggerEnum, String> {

	/**
	 *
	 */
	public Trigger(TriggerEnum val) {
		this.value = val;
	}
}

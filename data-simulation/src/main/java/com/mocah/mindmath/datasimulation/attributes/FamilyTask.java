/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.FamilyTaskEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class FamilyTask extends Attribute<String> {

	/**
	 *
	 */
	public FamilyTask(FamilyTaskEnum val) {
		this.value = val;
	}

}

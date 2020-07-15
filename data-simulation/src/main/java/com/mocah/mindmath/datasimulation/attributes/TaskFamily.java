/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.TaskFamilyEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class TaskFamily extends Attribute<String> {

	/**
	 *
	 */
	public TaskFamily(TaskFamilyEnum val) {
		this.value = val;
	}

}

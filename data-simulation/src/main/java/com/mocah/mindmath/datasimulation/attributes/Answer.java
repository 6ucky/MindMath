/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.AnswerEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Answer extends Attribute<AnswerEnum, Boolean> {

	/**
	 *
	 */
	public Answer(AnswerEnum val) {
		this.value = val;
	}
}

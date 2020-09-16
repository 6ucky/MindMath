/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.ErrorCodeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class ErrorCode extends Attribute<ErrorCodeEnum, String> {

	/**
	 *
	 */
	public ErrorCode(ErrorCodeEnum val) {
		this.value = val;
	}
}

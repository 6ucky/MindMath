/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.GeneratorEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Generator extends Attribute<String> {

	/**
	 *
	 */
	public Generator(GeneratorEnum val) {
		this.value = val;
	}
}

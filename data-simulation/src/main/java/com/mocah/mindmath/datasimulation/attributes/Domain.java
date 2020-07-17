/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.DomainEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Domain extends Attribute<DomainEnum, String> {

	/**
	 *
	 */
	public Domain(DomainEnum val) {
		this.value = val;
	}
}

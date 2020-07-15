/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.AttributeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public abstract class Attribute<E> implements IAttribute<E> {
	protected AttributeEnum<E> value;

	/**
	 *
	 */
	public Attribute() {
	}

	@Override
	public E getValue() {
		return this.value.getValue();
	}
}

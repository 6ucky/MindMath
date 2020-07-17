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
	public AttributeEnum<E> getEnum() {
		return this.value;
	}

	@Override
	public E getValue() {
		return this.value.getValue();
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
}

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.AttributeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 * @param <E> the contained enum class
 * @param <T> the used value type
 */
public abstract class Attribute<E extends Enum<E>, T> implements IAttribute<E, T> {
	protected AttributeEnum<E, T> value;

	/**
	 *
	 */
	public Attribute() {
	}

	@Override
	public AttributeEnum<E, T> getEnum() {
		return this.value;
	}

	@Override
	public T getValue() {
		return this.value.getValue();
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
}

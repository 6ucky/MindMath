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
public interface IAttribute<E extends Enum<E>, T> {
	public AttributeEnum<E, T> getEnum();

	public T getValue();
}

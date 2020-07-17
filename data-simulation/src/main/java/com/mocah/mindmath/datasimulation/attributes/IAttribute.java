/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

import com.mocah.mindmath.datasimulation.attributes.constraints.in.AttributeEnum;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IAttribute<E> {
	public AttributeEnum<E> getEnum();

	public E getValue();
}

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface AttributeEnum<E> {
	/**
	 * Get the enum value as object
	 *
	 * @return
	 */
	public E getValue();

	@Override
	public String toString();
}

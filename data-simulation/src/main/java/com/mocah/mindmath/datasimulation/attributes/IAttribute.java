/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public interface IAttribute<E> {
	public E getValue();

	@Override
	String toString();
}

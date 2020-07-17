/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 * @param <E> the declaring enum class
 * @param <T> the used value type
 */
public interface AttributeEnum<E extends Enum<E>, T> {
	/**
	 * Get the enum as enum
	 *
	 * @return
	 */
	public E getThis();

	/**
	 * Get the enum value as declared value type object
	 *
	 * @return
	 */
	public T getValue();

	Class<E> getDeclaringClass();

	@Override
	public String toString();
}

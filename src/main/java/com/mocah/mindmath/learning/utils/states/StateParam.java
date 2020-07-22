package com.mocah.mindmath.learning.utils.states;

import java.io.Serializable;
import java.util.Objects;

/**
 * Implement parameters
 *
 * @author Thibaut SIMON-FINE
 *
 * @param <T> the type of the parameter
 */
public class StateParam<T extends Object & Serializable> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -6221358004680246207L;

	private T value;

	/**
	 * Instantiate with a value for this parameter
	 *
	 * @param val
	 */
	public StateParam(T val) {
		this.value = val;
	}

	/**
	 * Set a new value
	 *
	 * @param val
	 */
	public void setValue(T val) {
		this.value = val;
	}

	/**
	 * Get the value of this parameter
	 *
	 * @return
	 */
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateParam<?> other = (StateParam<?>) obj;
		return Objects.equals(value, other.value);
	}
}

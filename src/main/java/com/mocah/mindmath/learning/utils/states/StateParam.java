package com.mocah.mindmath.learning.utils.states;

import java.io.Serializable;

/**
 * Implement parameters
 *
 * @author Thibaut SIMON-FINE
 *
 * @param <T> the type of the parameter
 */
public class StateParam<T extends Serializable> implements Serializable {
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
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

		if (this.value == null) {
			if (other.getValue() != null)
				return false;
		} else {
			if (!value.equals(other.getValue()))
				return false;
		}

		return true;
	}
}

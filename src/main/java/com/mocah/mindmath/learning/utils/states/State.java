/**
 *
 */
package com.mocah.mindmath.learning.utils.states;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * The state implementation for mindmath learning system Contains a Map of
 * key->values
 *
 * @author Thibaut SIMON-FINE
 *
 */
public class State implements IState {
	/**
	 *
	 */
	private static final long serialVersionUID = -4248467308854317569L;

	private LinkedHashMap<String, StateParam<?>> params;

	/**
	 * Instantiate a State which should be filled with <code>putParam</code> method
	 */
	public State() {
		this.params = new LinkedHashMap<>();
	}

	/**
	 * Get all the parameters
	 *
	 * @return
	 */
	public HashMap<String, StateParam<?>> getParams() {
		return params;
	}

	/**
	 * Add/replace a parameter with a key and it's value
	 *
	 * @param <T>
	 * @param key a string key
	 * @param val the value of custom type
	 */
	public <T extends Object & Serializable> void putParam(String key, T val) {
		params.put(key, new StateParam<>(val));
	}

	@Override
	public String toString() {
		return params.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(params);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;

		if (this.params == null) {
			if (other.getParams() != null)
				return false;
		} else {
			if (other.getParams() == null)
				return false;

			// Check if all common keys are equals
			Set<String> common = new HashSet<>(this.params.keySet());
			common.retainAll(other.getParams().keySet());

			for (String key : common) {
				StateParam<?> param = this.params.get(key);
				StateParam<?> paramOther = other.getParams().get(key);

				if (param == null) {
					if (paramOther != null)
						return false;
				} else {
					if (!param.equals(paramOther))
						return false;
				}
			}
		}

		return true;
	}
}

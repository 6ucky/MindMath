/**
 *
 */
package com.mocah.mindmath.learning.utils.states;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tsimo
 *
 */
public class State implements IState {
	Map<String, StateParam<?>> params;

	/**
	 *
	 */
	public State() {
		this.params = new HashMap<>();
	}

	public <T> void putParam(String key, T val) {
		params.put(key, new StateParam<>(val));
	}

	@Deprecated
	public <T> T get(String key) {
		return (T) params.get(key);
	}

	@Override
	public String toString() {
		return params.toString();
	}

}

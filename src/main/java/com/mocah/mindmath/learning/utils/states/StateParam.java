package com.mocah.mindmath.learning.utils.states;

public class StateParam<T> {
	private T value;

	public StateParam(T val) {
		this.value = val;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}

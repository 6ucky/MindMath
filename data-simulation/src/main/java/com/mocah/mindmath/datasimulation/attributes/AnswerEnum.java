/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum AnswerEnum {
	NULL(null), TRUE(Boolean.TRUE), FALSE(Boolean.FALSE);

	private final Boolean boolValue;

	private AnswerEnum(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	public Boolean getBoolValue() {
		return this.boolValue;
	}

	@Override
	public String toString() {
		return "" + this.boolValue;
	}
}

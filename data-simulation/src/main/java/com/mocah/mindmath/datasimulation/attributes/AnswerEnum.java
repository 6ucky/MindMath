/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum AnswerEnum {
	TRUE(true), FALSE(false);

	private final boolean boolValue;

	private AnswerEnum(boolean boolValue) {
		this.boolValue = boolValue;
	}

	public boolean getBoolValue() {
		return this.boolValue;
	}

	@Override
	public String toString() {
		return "" + this.boolValue;
	}
}

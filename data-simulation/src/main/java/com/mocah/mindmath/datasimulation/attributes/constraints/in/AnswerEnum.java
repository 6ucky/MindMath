/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum AnswerEnum implements AttributeEnum<Boolean> {
	NULL(null), TRUE(Boolean.TRUE), FALSE(Boolean.FALSE);

	private final Boolean boolValue;

	private AnswerEnum(Boolean boolValue) {
		this.boolValue = boolValue;
	}

	@Override
	public Boolean getValue() {
		return this.boolValue;
	}

	@Override
	public String toString() {
		return "" + this.boolValue;
	}
}

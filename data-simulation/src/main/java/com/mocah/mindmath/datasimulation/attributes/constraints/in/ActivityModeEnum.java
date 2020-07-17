/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum ActivityModeEnum implements AttributeEnum<ActivityModeEnum, Integer> {
	A0(0), A1(1), A2(2);

	private final Integer intValue;

	private ActivityModeEnum(int intValue) {
		this.intValue = intValue;
	}

	@Override
	public ActivityModeEnum getThis() {
		return this;
	}

	@Override
	public Integer getValue() {
		return this.intValue;
	}

	@Override
	public String toString() {
		return "" + this.intValue;
	}
}

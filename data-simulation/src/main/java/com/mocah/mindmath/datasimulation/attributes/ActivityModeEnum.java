/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum ActivityModeEnum {
	A0(0), A1(1), A2(2);

	private final int intValue;

	private ActivityModeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	@Override
	public String toString() {
		return "" + this.intValue;
	}
}

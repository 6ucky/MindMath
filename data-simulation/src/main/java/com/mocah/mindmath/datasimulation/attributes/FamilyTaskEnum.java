/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum FamilyTaskEnum {
	FT3_1("ft3.1"), FT8_3_1("ft8.3.1");

	private final String strValue;

	private FamilyTaskEnum(String strValue) {
		this.strValue = strValue;
	}

	public String getStrValue() {
		return this.strValue;
	}

	@Override
	public String toString() {
		return this.strValue;
	}
}

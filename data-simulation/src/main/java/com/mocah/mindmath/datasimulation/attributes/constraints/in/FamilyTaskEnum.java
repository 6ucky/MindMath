/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum FamilyTaskEnum implements AttributeEnum<String> {
	FT3_1("ft3.1"), FT8_3_1("ft8.3.1");

	private final String strValue;

	private FamilyTaskEnum(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public String getValue() {
		return this.strValue;
	}

	@Override
	public String toString() {
		return this.strValue;
	}
}

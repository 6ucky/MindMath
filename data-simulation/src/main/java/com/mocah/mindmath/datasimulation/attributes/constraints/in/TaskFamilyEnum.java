/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum TaskFamilyEnum implements AttributeEnum<TaskFamilyEnum, String> {
	FT3_1("ft3.1"), FT3_2_1("ft3.2.1"), FT3_2_2("ft3.2.2"), FT3_2_3("ft3.2.3"), FT6_1_1("ft6.1.1"), FT6_1_2("ft6.1.2"),
	FT6_1_3("ft6.1.3"), FT6_2_1("ft6.2.1"), FT6_2_2("ft6.2.2"), FT6_2("ft6.2"), FT7_1("ft7.1"), FT7_2("ft7.2"),
	FT7_3("ft7.3"), FT8_1("ft8.1"), FT8_2("ft8.2"), FT8_3_1("ft8.3.1"), FT8_3_2("ft8.3.2"), FT8_3_3("ft8.3.3"),
	FT9_1("ft9.1"), FT10_1("ft10.1");

	private final String strValue;

	private TaskFamilyEnum(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public TaskFamilyEnum getThis() {
		return this;
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

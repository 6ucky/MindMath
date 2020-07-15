/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum DomainEnum implements AttributeEnum<String> {
	ALGEBRA("algebre"), GEOMETRY("geometrie");

	private final String strValue;

	private DomainEnum(String strValue) {
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

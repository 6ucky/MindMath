/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum DomainEnum implements AttributeEnum<DomainEnum, String> {
	ALGEBRA("algebre"), GEOMETRY("geometrie");

	private final String strValue;

	private DomainEnum(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public DomainEnum getThis() {
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

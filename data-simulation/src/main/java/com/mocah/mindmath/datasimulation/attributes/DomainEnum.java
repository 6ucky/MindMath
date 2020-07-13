/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum DomainEnum {
	ALGEBRA("algebre"), GEOMETRY("geometrie");

	private final String strValue;

	private DomainEnum(String strValue) {
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

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum TriggerEnum implements AttributeEnum<String> {
	VALIDATE("bouton-valider"), HELP("bouton-aide");

	private final String strValue;

	private TriggerEnum(String strValue) {
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

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum TriggerEnum {
	VALIDATE("bouton-valider"), HELP("bouton-aide");

	private final String strValue;

	private TriggerEnum(String strValue) {
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

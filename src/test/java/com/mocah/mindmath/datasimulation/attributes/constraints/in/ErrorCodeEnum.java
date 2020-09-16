/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum ErrorCodeEnum implements AttributeEnum<ErrorCodeEnum, String> {
	NULL(""), CONFUSION_RATIONNEL("ce_confusionRationnel"), NON_GESTION_OPPOSE("ce_nonGestionOppose"),
	INV_NUM_DEN("ce_inversionNumerateurDenominateur"), CONCAT("ce_concatenation"), OPPOSE("ce_oppose");

	private final String strValue;

	private ErrorCodeEnum(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public ErrorCodeEnum getThis() {
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

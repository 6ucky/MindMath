/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum GeneratorEnum implements AttributeEnum<GeneratorEnum, String> {
	RES_EQ_PERMIER_DEGRE("resoudreEquationPremierDegre");

	private final String strValue;

	private GeneratorEnum(String strValue) {
		this.strValue = strValue;
	}

	@Override
	public GeneratorEnum getThis() {
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

/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes.constraints.in;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum ErrorCodeEnum implements AttributeEnum<String> {
	NULL(""), ERR1("ce_err1"), ERR2("ce_err2"), ERR3("ce_err3"), ERR4("ce_err4"), ERR5("ce_err5");

	private final String strValue;

	private ErrorCodeEnum(String strValue) {
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

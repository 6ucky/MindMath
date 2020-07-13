/**
 *
 */
package com.mocah.mindmath.datasimulation.attributes;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum ErrorCodeEnum {
	ERR1("ce_err1"), ERR2("ce_err2"), ERR3("ce_err3"), ERR4("ce_err4"), ERR5("ce_err5");

	private final String strValue;

	private ErrorCodeEnum(String strValue) {
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

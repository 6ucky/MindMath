/**
 *
 */
package com.mocah.mindmath.decisiontree;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Vars {
	private VarsSourceType source;
	private String key;

	public VarsSourceType getSource() {
		return source;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "Var from " + this.source + " with key " + this.key;
	}
}

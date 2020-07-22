/**
 *
 */
package com.mocah.mindmath.decisiontree;

import java.util.Objects;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public class Vars {
	private String name;
	private VarsSourceType source;
	private String key;

	public String getName() {
		return name;
	}

	public VarsSourceType getSource() {
		return source;
	}

	public String getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, name, source);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vars other = (Vars) obj;
		return Objects.equals(key, other.key) && Objects.equals(name, other.name) && source == other.source;
	}

	@Override
	public String toString() {
		return "Var '" + this.name + "' from " + this.source + " with key " + this.key;
	}
}

/**
 *
 */
package com.mocah.mindmath.decisiontree;

import com.google.gson.annotations.SerializedName;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum NodeType {
	@SerializedName("state")
	STATE,

	@SerializedName("decision")
	DECISION,

	@SerializedName("feedback")
	FEEDBACK
}

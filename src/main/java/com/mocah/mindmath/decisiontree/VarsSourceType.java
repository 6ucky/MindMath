/**
 *
 */
package com.mocah.mindmath.decisiontree;

import com.google.gson.annotations.SerializedName;

/**
 * @author Thibaut SIMON-FINE
 *
 */
public enum VarsSourceType {
	@SerializedName("Task")
	TASK,

	@SerializedName("Sensor")
	SENSOR,

	@SerializedName("Param")
	PARAM,

	@SerializedName("Log")
	LOG
}

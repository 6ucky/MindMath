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
	@SerializedName(value = "Task", alternate = { "task" })
	TASK,

	@SerializedName(value = "Sensor", alternate = { "sensor" })
	SENSOR,

	@SerializedName(value = "Param", alternate = { "param" })
	PARAM,

	@SerializedName(value = "Log", alternate = { "log" })
	LOG,

	@SerializedName(value = "Method", alternate = { "method" })
	CUSTOM_METHOD
}

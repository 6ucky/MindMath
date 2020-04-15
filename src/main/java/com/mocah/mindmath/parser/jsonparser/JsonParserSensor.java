package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

/**
 * @author	Yan Wang
 * @since	10/04/2020
 */

public class JsonParserSensor extends JsonParserFactory{

	private final JsonObject sensorsObject;
	
	public JsonParserSensor(String data) {
		super(data);
		JsonObject emptyobject = new JsonObject();
		this.sensorsObject = rootObject.has(SENSOR) ? rootObject.get(SENSOR).getAsJsonObject() : emptyobject;
	}
	
	@Override
	public JsonObject getObject() {
		return this.sensorsObject;
	}

	public Sensors getSensor() {
		Sensors sensorsClass = new Sensors(getValueAsString(super.getObject(),getTASK_ID()));
		if(rootObject.has(SENSOR))
		{
			sensorsClass = new Sensors(getValueAsString(super.getObject(),getTASK_ID()),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_1),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_2),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_3),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_BOOL_RJ),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_NB_TEMPS),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_NB_VALIDER),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_NB_EFFACER),
				getValueAsString(sensorsObject,SENSOR_CAPTEUR_NB_AIDE),
				getValueAsString(sensorsObject,SENSOR_DOMAIN),
				getValueAsString(sensorsObject,SENSOR_GENERATOR),
				getValueAsString(sensorsObject,SENSOR_TASKFAMILY),
				getValueAsString(sensorsObject,SENSOR_CORRECTANSWER),
				getValueAsString(sensorsObject,SENSOR_CODEERROR)
			);
		}
		return sensorsClass;
	}
}

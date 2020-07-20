package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.entity.task.Sensors;

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
		Sensors sensorsClass = new Sensors();
		if(rootObject.has(SENSOR))
		{
			sensorsClass = new Sensors(
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_1),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_2),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_BOOL_RF_CO2_3),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_BOOL_RJ),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_NB_TEMPS),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_NB_VALIDER),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_NB_EFFACER),
					getValueforDB(sensorsObject,SENSOR_CAPTEUR_NB_AIDE),
					getValueforDB(sensorsObject,SENSOR_LEARNER_ID),
					getValueforDB(sensorsObject,SENSOR_DOMAIN),
					getValueforDB(sensorsObject,SENSOR_GENERATOR),
					getValueforDB(sensorsObject,SENSOR_TASKFAMILY),
					getValueforDB(sensorsObject,SENSOR_CORRECTANSWER),
					getValueforDB(sensorsObject,SENSOR_CODEERROR),
					getValueforDB(sensorsObject,SENSOR_ACTIVITYMODE)
			);
		}
		return sensorsClass;
	}
}

package com.mocah.mindmath.parser.jsonparser;

import com.google.gson.JsonObject;
import com.mocah.mindmath.server.controller.cabri.CabriVersion;
import com.mocah.mindmath.server.entity.task.Sensors;

/**
 * @author Yan Wang
 * @since 10/04/2020
 */

public class JsonParserSensor extends JsonParserFactory {
	private final JsonObject sensorsObject;

	public JsonParserSensor(String data) {
		super(data);
		JsonObject emptyobject = new JsonObject();
		this.sensorsObject = rootObject.has(JsonParserKeys.SENSOR)
				? rootObject.get(JsonParserKeys.SENSOR).getAsJsonObject()
				: emptyobject;
	}

	@Override
	public JsonObject getObject() {
		return this.sensorsObject;
	}

	public Sensors getSensor(CabriVersion version) throws JsonParserCustomException {
		Sensors sensorsClass = new Sensors();
		if (rootObject.has(JsonParserKeys.SENSOR)) {
			switch(version)
			{
			case v1_0:
				sensorsClass = new Sensors(getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_1),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_2),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RF_CO2_3),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_BOOL_RJ),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_TEMPS),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_VALIDER),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_EFFACER),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CAPTEUR_NB_AIDE),
						getValueforDB(sensorsObject, JsonParserKeys.TASK_NAME),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_LEARNER_ID),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_DOMAIN),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_GENERATOR),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_TASKFAMILY),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CORRECTANSWER),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CODEERROR),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_ACTIVITYMODE),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_MAXNMFB));
				break;
			case v1_1:
				sensorsClass = new Sensors(
						//id_task may be null
						getValueforDB(sensorsObject, JsonParserKeys.TASK_NAME),
						getValueAsString(sensorsObject, JsonParserKeys.SENSOR_LEARNER_ID),
						getValueAsString(sensorsObject, JsonParserKeys.SENSOR_DOMAIN),
						getValueAsString(sensorsObject, JsonParserKeys.SENSOR_GENERATOR),
						getValueAsString(sensorsObject, JsonParserKeys.SENSOR_TASKFAMILY),
						getValueAsString(sensorsObject, JsonParserKeys.SENSOR_CORRECTANSWER),
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_CODEERROR),
						//activity mode may be null
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_ACTIVITYMODE),
						//maxFB is 4 if null
						getValueforDB(sensorsObject, JsonParserKeys.SENSOR_MAXNMFB));
				break;
			}
			
		}

		return sensorsClass;
	}
}

package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Params;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;
import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	09/03/2020
 */
public class JsonParserFactory extends JsonParserKeys implements ParserFactory <Task>{
	
	protected final JsonObject rootObject;
	
	//constructor of jsonparser
	public JsonParserFactory(String data) {
		this.rootObject = JsonParser.parseString(data).getAsJsonObject();
	}
	
	
	public String getTaskId()
	{
		return rootObject.has(TASK_ID) ? rootObject.get(TASK_ID).getAsString() : null;
	}
	
	public String getTaskName()
	{
		return rootObject.has(TASK_NAME) ? rootObject.get(TASK_NAME).getAsString() : null;
	}
	
	public String getTrigger()
	{
		return rootObject.has(TASK_TRIGGER) ? rootObject.get(TASK_TRIGGER).getAsString() : null;
	}
	
	// parse JSON file into Derby database
	public static Task parseCabri(String data) throws JsonParserException
	{
		JsonObject root = JsonParser.parseString(data).getAsJsonObject();
		
		//if there is no ID return empty object
		Task emptytask = new Task();
		if(!root.has(TASK_ID))
			return emptytask;
		
		String taskId = root.get(TASK_ID).getAsString();
		
		Params paramsClass = new Params();
		if(root.has(PARAMS))
		{
			JsonObject params = root.get(PARAMS).getAsJsonObject();
			paramsClass = new Params(taskId, 
				params.has(PARAMS_VT_2_1) ? params.get(PARAMS_VT_2_1).getAsLong() : -1, 
				params.has(PARAMS_VT_2_2) ? params.get(PARAMS_VT_2_2).getAsLong() : -1, 
				params.has(PARAMS_VT_2_3) ? params.get(PARAMS_VT_2_3).getAsBoolean() : false, 
				params.has(PARAMS_VT_2_4) ? params.get(PARAMS_VT_2_4).getAsBoolean() : false
			);
		}
		
		Sensors sensorsClass = new Sensors();
		if(root.has(SENSOR))
		{
			JsonObject sensors = root.get(SENSOR).getAsJsonObject();
			String nb_aide = null;
			if(sensors.has(SENSOR_CAPTEUR_NB_AIDE))
			{
				if(!sensors.get(SENSOR_CAPTEUR_NB_AIDE).isJsonNull())
					nb_aide = sensors.get(SENSOR_CAPTEUR_NB_AIDE).getAsString();
			}
			sensorsClass = new Sensors(taskId,
				sensors.has(SENSOR_CAPTEUR_BOOL_RF_CO2_1) ? sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_1).getAsBoolean() : false, 
				sensors.has(SENSOR_CAPTEUR_BOOL_RF_CO2_2) ? sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_2).getAsBoolean() : false,
				sensors.has(SENSOR_CAPTEUR_BOOL_RF_CO2_3) ? sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_3).getAsBoolean() : false, 
				sensors.has(SENSOR_CAPTEUR_BOOL_RJ) ? sensors.get(SENSOR_CAPTEUR_BOOL_RJ).getAsBoolean() : false, 
				sensors.has(SENSOR_CAPTEUR_NB_TEMPS) ? sensors.get(SENSOR_CAPTEUR_NB_TEMPS).getAsLong() : -1, 
				sensors.has(SENSOR_CAPTEUR_NB_VALIDER) ? sensors.get(SENSOR_CAPTEUR_NB_VALIDER).getAsLong() : -1, 
				sensors.has(SENSOR_CAPTEUR_NB_EFFACER) ? sensors.get(SENSOR_CAPTEUR_NB_EFFACER).getAsLong() : -1,
				nb_aide,
				sensors.has(SENSOR_DOMAIN) ? sensors.get(SENSOR_DOMAIN).getAsString() : null,
				sensors.has(SENSOR_GENERATOR) ? sensors.get(SENSOR_GENERATOR).getAsString() : null,
				sensors.has(SENSOR_TASKFAMILY) ? sensors.get(SENSOR_TASKFAMILY).getAsString() : null,
				sensors.has(SENSOR_CORRECTANSWER) ? sensors.get(SENSOR_CORRECTANSWER).getAsBoolean() :false,
				sensors.has(SENSOR_CODEERROR) ? sensors.get(SENSOR_CODEERROR).getAsString() : null
			);
		}
		
		List<Log> logsList = new ArrayList<Log>();
		if(root.has(LOG))
		{
			JsonArray logs = root.get(LOG).getAsJsonArray();
			for(int i = 0; i < logs.size(); i++)
			{
				JsonObject tempObject = logs.get(i).getAsJsonObject();
				Log temp = new Log(taskId + "-" + i, 
						tempObject.has(LOG_TIME) ? tempObject.get(LOG_TIME).getAsLong() : -1,
						tempObject.has(LOG_TYPE) ? tempObject.get(LOG_TYPE).getAsString() : null,
						tempObject.has(LOG_NAME) ? tempObject.get(LOG_NAME).getAsString() : null,
						tempObject.has(LOG_ACTION) ? tempObject.get(LOG_ACTION).getAsString() : null);
				logsList.add(temp);
			}
		}
		
		Task tasks = new Task(taskId, 
				root.has(TASK_NAME) ? root.get(TASK_NAME).getAsString() : null, 
				root.has(TASK_TRIGGER) ? root.get(TASK_TRIGGER).getAsString() : null, 
				sensorsClass, 
				paramsClass, 
				logsList);
		
		return tasks;
	}

	/**
	 * @deprecated use static method parseCabri 
	 */
	@Override
	public Task parse(String data) throws JsonParserException {
		
		return parseCabri(data);
	}
	
}

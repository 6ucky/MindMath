package com.mocah.mindmath.parser.jsonparser;

import java.util.ArrayList;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mocah.mindmath.learning.exceptions.JsonParserException;
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
	
	private final JsonObject rootObject;
	private final JsonObject paramsObject;
	private final JsonObject sensorsObject;
	private final JsonArray logsObject;
	
	public JsonParserFactory(String data) {
		this.rootObject = JsonParser.parseString(data).getAsJsonObject();
		this.paramsObject = rootObject.get(PARAMS).getAsJsonObject();
		this.sensorsObject = rootObject.get(SENSOR).getAsJsonObject();
		this.logsObject = rootObject.get(LOG).getAsJsonArray();
	}
	
	public String getTaskId()
	{
		return rootObject.get(TASK_ID).getAsString();
	}
	
	public String getTaskName()
	{
		return rootObject.get(TASK_NAME).getAsString();
	}
	
	public String getTrigger()
	{
		return rootObject.get(TASK_TRIGGER).getAsString();
	}
	
	public long getVT_2_1()
	{
		return paramsObject.get(PARAMS_VT_2_1).getAsLong();
	}
	
	public long getVT_2_2()
	{
		return paramsObject.get(PARAMS_VT_2_2).getAsLong();
	}
	
	public boolean getVT_2_3()
	{
		return paramsObject.get(PARAMS_VT_2_3).getAsBoolean();
	}
	
	public boolean getVT_2_4()
	{
		return paramsObject.get(PARAMS_VT_2_4).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_1()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_1).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_2()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_2).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_3()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_3).getAsBoolean();
	}
	
	public boolean getBOOL_RJ()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_BOOL_RJ).getAsBoolean();
	}
	
	public long getNB_TEMPS()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_NB_TEMPS).getAsLong();
	}
	
	public long getNB_VALIDER()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_NB_VALIDER).getAsLong();
	}
	
	public long getNB_EFFACER()
	{
		return sensorsObject.get(SENSOR_CAPTEUR_NB_EFFACER).getAsLong();
	}
	
	public String getNB_AIDE()
	{
		if(sensorsObject.get(SENSOR_CAPTEUR_NB_AIDE).isJsonNull())
			return "null";
		else
			return sensorsObject.get(SENSOR_CAPTEUR_NB_AIDE).getAsString();
	}
	
	public List<Log> getLogs()
	{
		List<Log> logs = new ArrayList<Log>();
		for(int i = 0; i < logsObject.size(); i++)
		{
			JsonObject tempObject = logsObject.get(i).getAsJsonObject();
			Log temp = new Log(getTaskId() + "-" + i, 
					tempObject.get(LOG_TIME).getAsLong(),
					tempObject.get(LOG_TYPE).getAsString(),
					tempObject.get(LOG_NAME).getAsString(),
					tempObject.get(LOG_ACTION).getAsString());
			logs.add(temp);
		}
		
		return logs;
	}
	
	// parse JSON file into Derby database
	public static Task parseCabri(String data) throws JsonParserException
	{		
		JsonObject root = JsonParser.parseString(data).getAsJsonObject();
		JsonObject params = root.get(PARAMS).getAsJsonObject();
		JsonObject sensors = root.get(SENSOR).getAsJsonObject();
		JsonArray logs = root.get(LOG).getAsJsonArray();
		String taskId = root.get(TASK_ID).getAsString();
		String nb_aide = null;
		if(!sensors.get(SENSOR_CAPTEUR_NB_AIDE).isJsonNull())
			nb_aide = sensors.get(SENSOR_CAPTEUR_NB_AIDE).getAsString();
		
		Params paramsClass = new Params(taskId, 
				params.get(PARAMS_VT_2_1).getAsLong(), 
				params.get(PARAMS_VT_2_2).getAsLong(), 
				params.get(PARAMS_VT_2_3).getAsBoolean(), 
				params.get(PARAMS_VT_2_4).getAsBoolean());
		
		Sensors sensorsClass = new Sensors(taskId,
				sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_1).getAsBoolean(), 
				sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_2).getAsBoolean(),
				sensors.get(SENSOR_CAPTEUR_BOOL_RF_CO2_3).getAsBoolean(), 
				sensors.get(SENSOR_CAPTEUR_BOOL_RJ).getAsBoolean(), 
				sensors.get(SENSOR_CAPTEUR_NB_TEMPS).getAsLong(), 
				sensors.get(SENSOR_CAPTEUR_NB_VALIDER).getAsLong(), 
				sensors.get(SENSOR_CAPTEUR_NB_EFFACER).getAsLong(), 
				nb_aide);
		
		List<Log> logsList = new ArrayList<Log>();
		for(int i = 0; i < logs.size(); i++)
		{
			JsonObject tempObject = logs.get(i).getAsJsonObject();
			Log temp = new Log(taskId + "-" + i, 
					tempObject.get(LOG_TIME).getAsLong(),
					tempObject.get(LOG_TYPE).getAsString(),
					tempObject.get(LOG_NAME).getAsString(),
					tempObject.get(LOG_ACTION).getAsString());
			logsList.add(temp);
		}
		
		Task tasks = new Task(taskId, 
				root.get(TASK_NAME).getAsString(), 
				root.get(TASK_TRIGGER).getAsString(), 
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
		
		Params params = new Params(getTaskId(), getVT_2_1(), getVT_2_2(), getVT_2_3(), getVT_2_4());
		
		Sensors sensors = new Sensors(getTaskId(), getBOOL_RF_CO2_1(), getBOOL_RF_CO2_2(),
				getBOOL_RF_CO2_3(), getBOOL_RJ(), getNB_TEMPS(), getNB_VALIDER(), getNB_EFFACER(), getNB_AIDE());
		
		Task tasks = new Task(getTaskId(), getTaskName(), getTrigger(), sensors, params, getLogs());
		
		return tasks;
	}
	
}

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
	
	private JsonObject rootObject;
	private JsonArray logsObject;
	
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
		return rootObject.get(PARAMS).getAsJsonObject().get(PARAMS_VT_2_1).getAsLong();
	}
	
	public long getVT_2_2()
	{
		return rootObject.get(PARAMS).getAsJsonObject().get(PARAMS_VT_2_2).getAsLong();
	}
	
	public boolean getVT_2_3()
	{
		return rootObject.get(PARAMS).getAsJsonObject().get(PARAMS_VT_2_3).getAsBoolean();
	}
	
	public boolean getVT_2_4()
	{
		return rootObject.get(PARAMS).getAsJsonObject().get(PARAMS_VT_2_4).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_1()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_BOOL_RF_CO2_1).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_2()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_BOOL_RF_CO2_2).getAsBoolean();
	}
	
	public boolean getBOOL_RF_CO2_3()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_BOOL_RF_CO2_3).getAsBoolean();
	}
	
	public boolean getBOOL_RJ()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_BOOL_RJ).getAsBoolean();
	}
	
	public long getNB_TEMPS()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_NB_TEMPS).getAsLong();
	}
	
	public long getNB_VALIDER()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_NB_VALIDER).getAsLong();
	}
	
	public long getNB_EFFACER()
	{
		return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_NB_EFFACER).getAsLong();
	}
	
	public String getNB_AIDE()
	{
		if(rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_NB_AIDE).isJsonNull())
			return "null";
		else
			return rootObject.get(SENSOR).getAsJsonObject().get(SENSOR_CAPTEUR_NB_AIDE).getAsString();
	}
	
	public List<Log> getLogs()
	{
		List<Log> logs = new ArrayList<Log>();
		for(int i = 0; i < logsObject.size(); i++)
		{
			Log temp = new Log();
			temp.setId(getTaskId() + "-" + i);
			temp.setTime(logsObject.get(i).getAsJsonObject().get(LOG_TIME).getAsLong());
			temp.setType(logsObject.get(i).getAsJsonObject().get(LOG_TYPE).getAsString());
			temp.setName(logsObject.get(i).getAsJsonObject().get(LOG_NAME).getAsString());
			temp.setAction(logsObject.get(i).getAsJsonObject().get(LOG_ACTION).getAsString());
			logs.add(temp);
		}
		
		return logs;
	}
	
	@Override
	public Task parse(String data) throws JsonParserException
	{
		JsonParser parser = new JsonParser();
		rootObject = parser.parse(data).getAsJsonObject();
		logsObject = rootObject.get(LOG).getAsJsonArray();
		
		Task tasks = new Task();
		tasks.setId(getTaskId());
		tasks.setTask(getTaskName());
		tasks.setTrigger(getTrigger());
		
		Params params = new Params();
		params.setId(getTaskId());
		params.setVT_2_1(getVT_2_1());
		params.setVT_2_2(getVT_2_2());
		params.setVT_2_3(getVT_2_3());
		params.setVT_2_4(getVT_2_4());
		tasks.setParams(params);
		
		Sensors sensors = new Sensors();
		sensors.setId(getTaskId());
		sensors.setCapteur_bool_RF_CO2_1(getBOOL_RF_CO2_1());
		sensors.setCapteur_bool_RF_CO2_2(getBOOL_RF_CO2_2());
		sensors.setCapteur_bool_RF_CO2_3(getBOOL_RF_CO2_3());
		sensors.setCapteur_bool_RJ(getBOOL_RJ());
		sensors.setCapteur_nb_temps(getNB_TEMPS());
		sensors.setCapteur_nb_valider(getNB_VALIDER());
		sensors.setCapteur_nb_effacer(getNB_EFFACER());
		sensors.setCapteur_nb_aide(getNB_AIDE());
		tasks.setSensors(sensors);
		
		if(logsObject.size() > 0)
		{
			tasks.setLog(getLogs());
		}
		
		return tasks;
	}
	
}

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

	public Sensors getSensor() {
		Sensors sensorsClass = new Sensors();
		if(rootObject.has(SENSOR))
		{
			sensorsClass = new Sensors(getTaskId(), getBOOL_RF_CO2_1(), getBOOL_RF_CO2_2(),
					getBOOL_RF_CO2_3(), getBOOL_RJ(), getNB_TEMPS(), getNB_VALIDER(), getNB_EFFACER(), getNB_AIDE(),
					getDOMAIN(), getGENERATOR(), getTASKFAMILY(), getCORRECTANSWER(), getCODEERROR());
		}
		return sensorsClass;
	}
	public boolean getBOOL_RF_CO2_1()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_BOOL_RF_CO2_1) ? sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_1).getAsBoolean() : false;
	}
	
	public boolean getBOOL_RF_CO2_2()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_BOOL_RF_CO2_2) ? sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_2).getAsBoolean() : false;
	}
	
	public boolean getBOOL_RF_CO2_3()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_BOOL_RF_CO2_3) ? sensorsObject.get(SENSOR_CAPTEUR_BOOL_RF_CO2_3).getAsBoolean() : false;
	}
	
	public boolean getBOOL_RJ()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_BOOL_RJ) ? sensorsObject.get(SENSOR_CAPTEUR_BOOL_RJ).getAsBoolean() : false;
	}
	
	public long getNB_TEMPS()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_NB_TEMPS) ? sensorsObject.get(SENSOR_CAPTEUR_NB_TEMPS).getAsLong() : -1;
	}
	
	public long getNB_VALIDER()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_NB_VALIDER) ? sensorsObject.get(SENSOR_CAPTEUR_NB_VALIDER).getAsLong() : -1;
	}
	
	public long getNB_EFFACER()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_NB_EFFACER)? sensorsObject.get(SENSOR_CAPTEUR_NB_EFFACER).getAsLong() : -1;
	}
	
	public String getNB_AIDE()
	{
		return sensorsObject.has(SENSOR_CAPTEUR_NB_AIDE) ? 
				(sensorsObject.get(SENSOR_CAPTEUR_NB_AIDE).isJsonNull() ? 
						null : sensorsObject.get(SENSOR_CAPTEUR_NB_AIDE).getAsString()) : null;
	}
	public String getDOMAIN()
	{
		return sensorsObject.has(SENSOR_DOMAIN)? sensorsObject.get(SENSOR_DOMAIN).getAsString() : null;
	}
	
	public String getGENERATOR()
	{
		return sensorsObject.has(SENSOR_GENERATOR)? sensorsObject.get(SENSOR_GENERATOR).getAsString() : null;
	}
	
	public String getTASKFAMILY()
	{
		return sensorsObject.has(SENSOR_TASKFAMILY)? sensorsObject.get(SENSOR_TASKFAMILY).getAsString() : null;
	}
	
	public boolean getCORRECTANSWER()
	{
		return sensorsObject.has(SENSOR_CORRECTANSWER)? sensorsObject.get(SENSOR_CORRECTANSWER).getAsBoolean() : false;
	}
	
	public String getCODEERROR()
	{
		return sensorsObject.has(SENSOR_CODEERROR)? sensorsObject.get(SENSOR_CODEERROR).getAsString() : null;
	}
}

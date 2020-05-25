package com.mocah.mindmath.repository;

import java.util.List;

import com.mocah.mindmath.server.cabri.jsondata.Log;
import com.mocah.mindmath.server.cabri.jsondata.Sensors;

public interface XAPIRepository {

	public String getAboutfromLearningLocker();
	
	public String getAllStatementfromLearningLocker();
	
	public String postStatementTEST (String id, Sensors sensors, List<Log> log);
	
}

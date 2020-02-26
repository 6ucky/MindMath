package com.MindMath.LearningLocker.Parser;

public interface LearningLockerParserFactory <T, ID> {
	
	public String getid(ID id);
	
	public String geturl();
	
	public String gettime(T task);
	
	public String getorganisation(T task);
	
	public String getname(T task);
	
	public String getstatement(T task);

}

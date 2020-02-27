package com.MindMath.Parser;

public interface LearningLockerParserFactory <T, ID> extends ParserFactory {
	
	public String getid(ID id);
	
	public String geturl();
	
	public String gettime(T task);
	
	public String getorganisation(T task);
	
	public String getname(T task);
	
	public String getstatement(T task);

}

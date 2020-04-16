package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.JsonParseCustomException;

public interface ParserFactory <Object>{

	public Object parse(String data) throws JsonParseCustomException;

}

package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.JsonParserException;

public interface ParserFactory <Object>{

	public Object parse(String data) throws JsonParserException;

}

package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;

public interface ParserFactory <Object>{

	public Object parse(String data) throws JsonParserCustomException;

}

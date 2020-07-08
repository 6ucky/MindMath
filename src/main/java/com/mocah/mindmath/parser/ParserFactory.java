package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;

public interface ParserFactory<T> {

	public Object parse(String data, String version) throws JsonParserCustomException;

}

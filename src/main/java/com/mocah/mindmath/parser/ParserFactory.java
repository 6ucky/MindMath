package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.CabriVersion;
import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;

public interface ParserFactory<T> {

	public Object parse(String data, CabriVersion version) throws JsonParserCustomException;

}

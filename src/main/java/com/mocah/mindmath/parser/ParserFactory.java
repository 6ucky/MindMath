package com.mocah.mindmath.parser;

import com.mocah.mindmath.parser.jsonparser.JsonParserCustomException;
import com.mocah.mindmath.server.controller.cabri.CabriVersion;

public interface ParserFactory<T> {

	public Object parse(String data, CabriVersion version) throws JsonParserCustomException;

}

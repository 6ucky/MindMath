package com.mocah.mindmath.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public interface LocalRouteRepository {
	
	public static InputStream readFileasInputStream(String route) {
		return LocalRouteRepository.class.getClassLoader().getResourceAsStream(route);
	}
	
	public static Reader readFileasReader(String route) {
		return new InputStreamReader(readFileasInputStream(route));
	}
	
	public static String readFileasString(String route) throws IOException {
		Reader reader = new InputStreamReader(LocalRouteRepository.class.getClassLoader().getResourceAsStream(route));
		StringBuilder textBuilder = new StringBuilder();
	    int i = 0;
	    while ((i = reader.read()) != -1) {
	        textBuilder.append((char) i);
	    }
	    return textBuilder.toString();
	}
}

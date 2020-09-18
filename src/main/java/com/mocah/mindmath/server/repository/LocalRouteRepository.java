package com.mocah.mindmath.server.repository;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public interface LocalRouteRepository {
	
	public static InputStream readFileasInputStream(String route) {
		return LocalRouteRepository.class.getClassLoader().getResourceAsStream(route);
	}
	
	public static Reader readFileasReader(String route){
		return new InputStreamReader(readFileasInputStream(route), Charset.forName("UTF8"));
	}
	
	public static String readFileasString(String route) throws IOException {
		Reader reader = new InputStreamReader(LocalRouteRepository.class.getClassLoader().getResourceAsStream(route), Charset.forName("UTF8"));
		StringBuilder textBuilder = new StringBuilder();
	    int i = 0;
	    while ((i = reader.read()) != -1) {
	        textBuilder.append((char) i);
	    }
	    return textBuilder.toString();
	}
	
	public static void writeFile(String data, String route) throws IOException {
		String path = URLDecoder.decode(LocalRouteRepository.class.getClassLoader().getResource(route).getPath(), StandardCharsets.UTF_8.toString());
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
		writer.write(data);
	    writer.close();
	}
}

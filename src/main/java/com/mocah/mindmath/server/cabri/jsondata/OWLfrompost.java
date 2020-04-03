package com.mocah.mindmath.server.cabri.jsondata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

public class OWLfrompost implements Serializable{
	
	private static final long serialVersionUID = -6736827762178310360L;
	
	public static String owldata;
	
	public static InputStream getInputStream()
	{
		return new ByteArrayInputStream(owldata.getBytes());
	}
	
}

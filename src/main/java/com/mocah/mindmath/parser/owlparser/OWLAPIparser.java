package com.mocah.mindmath.parser.owlparser;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

import com.mocah.mindmath.server.cabri.jsondata.OWLfrompost;

public class OWLAPIparser {
	
	public static List<String> listallresources(){
		Model model = ModelFactory.createDefaultModel() ;
		model.read(OWLfrompost.getInputStream(), "");
		List<String> results = new ArrayList<String>();
		ResIterator iter = model.listSubjects();
		Resource r = iter.nextResource();
		while(iter.hasNext())
		{
			results.add(r.toString());
			r = iter.nextResource();
		}
		return results;
	}

}

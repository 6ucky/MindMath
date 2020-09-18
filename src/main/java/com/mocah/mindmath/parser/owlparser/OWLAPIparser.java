package com.mocah.mindmath.parser.owlparser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import com.mocah.mindmath.parser.ParserFactory;
import com.mocah.mindmath.server.controller.cabri.CabriVersion;

/**
 * @author	Yan Wang
 * @since	06/04/2020
 */

public class OWLAPIparser implements ParserFactory <OWLparserRepo> {
	
	private Model model;
	
	//basic parser constructor
	public OWLAPIparser() {
		this.model = ModelFactory.createDefaultModel();
		this.model.read(getInputStream(), "");
	}
	
	//initialize OWL API parser, only used at first when we need the input of OWL file from POST.
	public OWLAPIparser(String data) {
		OWLparserRepo.owldata = data;
		this.model = ModelFactory.createDefaultModel();
		this.model.read(getInputStream(), "");
	}
	
	//transform into InputStream for the input of Jena parser
	private static InputStream getInputStream()
	{
		return new ByteArrayInputStream(OWLparserRepo.owldata.getBytes());
	}
	
	/**
	 * read the owl file based on object and identify resource and literal
	 * @return the list of node information
	 * @deprecated use listall method instead
	 */
	public List<String> listallObject(){
		List<String> results = new ArrayList<String>();
		NodeIterator iter = model.listObjects();
		while(iter.hasNext())
		{
			RDFNode n = iter.nextNode();
			String message = "";
			if(n.isAnon())
				message += " Anon";
			else if(n.isLiteral())
				message += n.asLiteral().getString() + " Literal";
			else if(n.isResource())
				message += n.asResource().getLocalName() + " Resource";
			else if(n.isURIResource())
				message += " URI";
			results.add(message);
		}
		return results;
	}
	
	/**
	 * list all the statements including resource, property and RDFNode
	 * @return the list of statement information
	 */
	public List<String> listall(){
		List<String> results = new ArrayList<String>();
		// list the statements in the Model
		StmtIterator iter = model.listStatements();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			String message = "";
		    Statement stmt      = iter.nextStatement();  // get next statement
		    Resource  subject   = stmt.getSubject();     // get the subject
		    Property  predicate = stmt.getPredicate();   // get the predicate
		    RDFNode   object    = stmt.getObject();      // get the object

		    message += "Resource:" + subject.getLocalName();
		    message += " Property:" + predicate.getLocalName() + " ";
		    if(object.isAnon())
				message += " Node:" + " -Anon";
			else if(object.isLiteral())
				message += " Node-Literal:" + object.asLiteral().getString();
			else if(object.isResource())
				message += " Node-Resource:" + object.asResource().getLocalName();
		    results.add(message);
		}
		return results;
	}
	
	/**
	 * find statement based on name
	 * @param name the name of the resource
	 * @return one statement
	 */
	public Statement getStatement(String name) {
		StmtIterator iter = model.listStatements();
		while(iter.hasNext())
		{
			Statement stmt      = iter.nextStatement();  // get next statement
			Resource  subject   = stmt.getSubject();     // get the subject
			if(subject.getLocalName().equals(name))
				return stmt;
		}
		return null;
	}
	
	/**
	 * find statement based on resource
	 * @param r the target resource
	 * @return one statement
	 */
	public Statement getStatement(Resource r) {
		StmtIterator iter = model.listStatements();
		while(iter.hasNext())
		{
			Statement stmt      = iter.nextStatement();  // get next statement
			Resource  subject   = stmt.getSubject();     // get the subject
			if(subject.equals(r))
				return stmt;
		}
		return null;
	}
	
	/**
	 * @param s the target statement
	 * @return the resource in this statement
	 */
	public Resource getResource(Statement s) {
		return s.getSubject();
	}
	
	/**
	 * @param s the target statement
	 * @return the property in this statement
	 */
	public Property getProperty(Statement s) {
		return s.getPredicate();
	}
	
	/**
	 * @param s the target statement
	 * @return the RDF node in this statement
	 */
	public RDFNode getNode(Statement s) {
		return s.getObject();
	}
	
	/**
	 * @param node the target RDF node
	 * @return the resource or the literal in this RDF node
	 */
	public <T> T getfromNode(RDFNode node) {
		if(node.isLiteral())
			return (T) node.asLiteral();
		else if(node.isResource())
			return (T) node.asResource();
		else
			return null;
	}

	/**
	 * Update or overwrite the data of owl file
	 */
	@Override
	public OWLparserRepo parse(String data, CabriVersion version) {
		OWLparserRepo.owldata = data;
		model.read(getInputStream(), "");
		return null;
	}

}

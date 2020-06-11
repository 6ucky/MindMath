package com.mocah.mindmath.server.cabri.feedback;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Feedbackjson implements Serializable{

	private static final long serialVersionUID = -9041203134380944632L;
	
	private final String idFbCabri;
	private final String idFb;
	private final String motivationalElementFb;
	private final String contentFb;
	private final String glossaryFb;
	
	//Test Feedback response
	public Feedbackjson(String id) throws IOException {
		this.idFbCabri = id;
		this.idFb = "F1.1";
		this.motivationalElementFb = String2GeneralHTML("Bravo!");
		this.contentFb = String2GeneralHTML("mindmath.lip6.fr/videos/ResolutionEquation.mp4");
		this.glossaryFb = String2GeneralHTML("hypertext");
	}
	
	public Feedbackjson(String id, String idF, String url, String motivationalElement, String solutionModel, String glossary) throws IOException {
		this.idFbCabri = id;
		this.idFb = idF;
		this.motivationalElementFb = String2GeneralHTML(motivationalElement);
		this.contentFb = String2GeneralHTML(solutionModel);
		this.glossaryFb = String2GeneralHTML(glossary);
	}

	private String String2GeneralHTML(String content) throws IOException {
		//Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/generalHTML.mustache");
		GeneralHTML fbhtml = new GeneralHTML(content);
		StringWriter writer = new StringWriter();
		//Executing the Mustache Template
		m.execute(writer, fbhtml).flush();
		return writer.toString();
	}
	
	public String getIdFbCabri() {
		return idFbCabri;
	}

	public String getMotivationalElementFb() {
		return motivationalElementFb;
	}

	public String getContentFb() {
		return contentFb;
	}

	public String getGlossaryFb() {
		return glossaryFb;
	}

	public String getIdFb() {
		return idFb;
	}

}

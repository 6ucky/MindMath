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
		this.contentFb = String2ContentFBHTML("","https://mindmath.lip6.fr/videos/ResolutionEquation.mp4","");
		this.glossaryFb = String2GlossaryFBHTML("Une propriété est bla bla.", "Ceci signifie bla bla.");
	}
	
	public Feedbackjson(String id, boolean correctness) {
		this.idFbCabri = id;
		this.contentFb = "";
		this.glossaryFb = "";
		if(correctness)
		{
			this.idFb = "F1.1";
			this.motivationalElementFb = "<h1>Bravo! Poursuis dans ta lancée!</h1>";
		}
		else
		{
			this.idFb = "F2.1";
			this.motivationalElementFb = "<h1>Ne baisse pas les bras, prend ton temps et réessaye!</h1>";
		}
	}
	
	public Feedbackjson(String id, String idF, String url, String motivationalElement, String default_img_url, 
			String video_url, String video_srt_url, String content_propriete, String content_preservation) throws IOException {
		this.idFbCabri = id;
		this.idFb = idF;
		this.motivationalElementFb = String2GeneralHTML(motivationalElement);
		this.contentFb = String2ContentFBHTML(default_img_url, video_url, video_srt_url);
		this.glossaryFb = String2GlossaryFBHTML(content_propriete, content_preservation);
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
	
	private String String2ContentFBHTML(String default_img_url, String video_url, String video_srt_url) throws IOException {
		//Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/contentFB.mustache");
		ContentFBHTML fbhtml = new ContentFBHTML(default_img_url, video_url, video_srt_url);
		StringWriter writer = new StringWriter();
		//Executing the Mustache Template
		m.execute(writer, fbhtml).flush();
		return writer.toString();
	}
	
	private String String2GlossaryFBHTML(String content_propriete, String content_preservation) throws IOException {
		//Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/glossaryFB.mustache");
		GlossaryFBHTML fbhtml = new GlossaryFBHTML(content_propriete, content_preservation);
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

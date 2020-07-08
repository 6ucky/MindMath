package com.mocah.mindmath.server.cabri.feedback;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class Feedbackjson implements Serializable {

	private static final long serialVersionUID = -9041203134380944632L;

	private final String idLearner;
	private final String idFbCabri;
	// the id of the actitvity family performed by the learner
	private final String idFamilytask;
	// feedbackId
	private final String idFeedback;
	private final String motivationalElementFb;
	private final String contentFb;
	private final String glossaryFb;

	// Test Feedback response
	public Feedbackjson(String id) throws IOException {
		this(id, "ft3.1", "0.0.0.0");
	}

	public Feedbackjson(String id, String idFamilytask, String idFeedback) throws IOException {
		this.idLearner = id;
		this.idFbCabri = "";
		this.idFamilytask = idFamilytask;
		this.idFeedback = idFeedback;
		this.motivationalElementFb = String2GeneralHTML("Bravo!");
		this.contentFb = String2ContentFBHTML("", "https://mindmath.lip6.fr/videos/ResolutionEquation.mp4", "",
				"video/mp4");
		HashMap<String, String> glossaryMap = new HashMap<>();
		glossaryMap.put("Une propriété est bla bla.", "Ceci signifie bla bla.");
		this.glossaryFb = String2GlossaryFBHTML(glossaryMap);
	}

	// Test for Feedbackcontent Database
	public Feedbackjson(String motivation, String content_url, String content_type, HashMap<String, String> glossaryMap)
			throws IOException {
		this.idLearner = "100";
		this.idFbCabri = "";
		this.idFamilytask = "ft3.1";
		this.idFeedback = "0.0.0.0";
		this.motivationalElementFb = String2GeneralHTML(motivation);
		if (content_type.equals("video/mp4")) {
			this.contentFb = String2ContentFBHTML("", content_url, "", content_type);
		} else {
			this.contentFb = String2ContentFBHTML(content_url, "", "", content_type);
		}
		this.glossaryFb = String2GlossaryFBHTML(glossaryMap);
	}

	// Test for Tralalere
	public Feedbackjson(String id, boolean correctness) {
		this.idLearner = id;
		this.idFbCabri = "";
		this.contentFb = "";
		this.glossaryFb = "";
		this.idFeedback = "0.0.0.0";
		if (correctness) {
			this.idFamilytask = "F1.1";
			this.motivationalElementFb = "<h1>Bravo! Poursuis dans ta lancée!</h1>";
		} else {
			this.idFamilytask = "F2.1";
			this.motivationalElementFb = "<h1>Ne baisse pas les bras, prend ton temps et réessaye!</h1>";
		}
	}

	public Feedbackjson(String idLearner, String idFbCabri, String idFamilytask, String idFeedback,
			String motivationalElement, String content_url,	String content_type, HashMap<String, String> glossaryMap) throws IOException {
		this.idLearner = idLearner;
		this.idFbCabri = idFbCabri;
		this.idFamilytask = idFamilytask;
		this.idFeedback = idFeedback;
		this.motivationalElementFb = String2GeneralHTML(motivationalElement);
		if (content_type.equals("video/mp4")) {
			this.contentFb = String2ContentFBHTML("", content_url, "", content_type);
		} else {
			this.contentFb = String2ContentFBHTML(content_url, "", "", content_type);
		}
		this.glossaryFb = String2GlossaryFBHTML(glossaryMap);
	}

	private String String2GeneralHTML(String content) throws IOException {
		// Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/generalHTML.mustache");
		GeneralHTML fbhtml = new GeneralHTML(content);
		StringWriter writer = new StringWriter();
		// Executing the Mustache Template
		m.execute(writer, fbhtml).flush();
		return writer.toString();
	}

	private String String2ContentFBHTML(String default_img_url, String video_url, String video_srt_url, String type)
			throws IOException {
		// Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		StringWriter writer = new StringWriter();
		switch (type) {
		case "video/mp4":
			Mustache m1 = mf.compile("mustache_template/contentFBVideo.mustache");
			ContentFBHTML fbhtml1 = new ContentFBHTML(default_img_url, video_url, video_srt_url);
			m1.execute(writer, fbhtml1).flush();
			return writer.toString();
		case "image":
			Mustache m2 = mf.compile("mustache_template/contentFBImage.mustache");
			ContentFBHTML fbhtml2 = new ContentFBHTML(default_img_url, "", "");
			m2.execute(writer, fbhtml2).flush();
			return writer.toString();
		}

		// Executing the Mustache Template
		return null;
	}

	private String String2GlossaryFBHTML(HashMap<String, String> glossaryMap) throws IOException {
		// Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/glossaryFB.mustache");
		String result = "";
		StringWriter writer = new StringWriter();
		Iterator<String> iterator = glossaryMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			GlossaryFBHTML fbhtml = new GlossaryFBHTML(key, glossaryMap.get(key));
			m.execute(writer, fbhtml).flush();
			result += writer.toString();
		}

		// Executing the Mustache Template
		return result;
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

	public String getIdLearner() {
		return idLearner;
	}

	public String getIdFamilytask() {
		return idFamilytask;
	}

	public String getIdFeedback() {
		return idFeedback;
	}

}

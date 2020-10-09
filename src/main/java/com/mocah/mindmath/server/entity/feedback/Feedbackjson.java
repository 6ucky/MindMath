package com.mocah.mindmath.server.entity.feedback;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.mocah.mindmath.learning.utils.states.IState;
import com.mocah.mindmath.learning.utils.values.IValue;

public class Feedbackjson implements Serializable {
	private static final long serialVersionUID = -9041203134380944632L;

	private final String idLearner;
	private final String idTask;
	// the id of the actitvity family performed by the learner
	private final String taskFamily;
	// feedbackId
	private final String idFb;
	private final String idFbCabri;
	private final String motivationalElementFb;
	private final String contentFb;
	private final String glossaryFb;
	private final boolean correctAnswer;
	private final double successScore;
	private final boolean closeTask;
	private final String closeTaskReason;

	// Verbose fields
	private String mode;
	private Double reward;
	private String modifiedState;
	private Map<String, Double> qvalues;

	// Test Feedback response
	public Feedbackjson(String id) throws IOException {
		this(id, "ft3.1", "0.0.0.0");
	}

	public Feedbackjson(String id, String idFamilytask, String idFeedback) throws IOException {
		this.idLearner = id;
		this.idTask = "";
		this.idFbCabri = "";
		this.taskFamily = idFamilytask;
		this.idFb = idFeedback;
		this.motivationalElementFb = String2GeneralHTML("Bravo!");
		this.contentFb = String2ContentFBHTML("", "https://mindmath.lip6.fr/videos/ResolutionEquation.mp4", "",
				"video/mp4");
		HashMap<String, String> glossaryMap = new HashMap<>();
		glossaryMap.put("Une propriété est bla bla.", "Ceci signifie bla bla.");
		this.glossaryFb = String2GlossaryFBHTML(glossaryMap);
		this.correctAnswer = false;
		this.successScore = 0.8;
		this.closeTask = false;
		this.closeTaskReason = null;
	}

	// Test for Tralalere
	public Feedbackjson(String id, boolean correctness) {
		this.idLearner = id;
		this.idTask = "";
		this.idFbCabri = "";
		this.contentFb = "";
		this.glossaryFb = "";
		this.idFb = "0.0.0.0";
		if (correctness) {
			this.taskFamily = "F1.1";
			this.motivationalElementFb = "<h1>Bravo! Poursuis dans ta lancée!</h1>";
		} else {
			this.taskFamily = "F2.1";
			this.motivationalElementFb = "<h1>Ne baisse pas les bras, prend ton temps et réessaye!</h1>";
		}
		this.correctAnswer = false;
		this.successScore = 0.8;
		this.closeTask = false;
		this.closeTaskReason = null;
	}

	// Test for Benjamin's table
	public Feedbackjson(String idLearner, String idFbCabri, String idFamilytask, String idFeedback,
			String motivationalElement, String content_url, String content_type, HashMap<String, String> glossaryMap)
			throws IOException {
		this(idLearner, "", idFbCabri, idFamilytask, idFeedback, motivationalElement, content_url, content_type, glossaryMap, false, 0.8, false, "6", "1", "");
	}
	
	//version 1.1 null feedback closeTask
	public Feedbackjson(String idLearner, String idTask, String idFbCabri, String idFamilytask,
			boolean correctAnswer, double successScore, String closeTaskReason) throws IOException {
		this.idLearner = idLearner;
		this.idTask = idTask;
		this.idFbCabri = idFbCabri;
		this.taskFamily = idFamilytask;
		this.idFb = "";
		this.motivationalElementFb = "";
		this.contentFb = "";
		this.glossaryFb = "";
		this.correctAnswer = correctAnswer;
		this.successScore = successScore;
		this.closeTask = true;
		this.closeTaskReason = closeTaskReason;
	}
	
	//version 1.1
	public Feedbackjson(String idLearner, String idTask, String idFbCabri, String idFamilytask, String idFeedback,
			String motivationalElement, String content_url, String content_type, HashMap<String, String> glossaryMap,
			boolean correctAnswer, double successScore, boolean closeTask, String leaf, String error_type,
			String statementRef) throws IOException {
		this.idLearner = idLearner;
		this.idTask = idTask;
		this.idFbCabri = idFbCabri;
		this.taskFamily = idFamilytask;
		this.idFb = idFeedback;
		this.motivationalElementFb = String2GeneralHTML1_1(motivationalElement);
		this.contentFb = String2ContentFBHTML(content_url, idFeedback, leaf, error_type, idLearner, idTask, statementRef)
				+ String2GlossaryFBHTML(glossaryMap, idFeedback, leaf, error_type, idLearner, idTask, statementRef);
		this.glossaryFb = String2GlossaryFBHTML(glossaryMap);
		this.correctAnswer = correctAnswer;
		this.successScore = successScore;
		this.closeTask = closeTask;
		this.closeTaskReason = null;
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
	
	private String String2GeneralHTML1_1(String content) throws IOException {
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/contentFB1_1.mustache");
		
		GeneralHTML fbhtml = new GeneralHTML(content);
		Map<String, Object> context = new HashMap<>();
	    context.put("motivation", fbhtml);
	    
		StringWriter writer = new StringWriter();
		m.execute(writer, context).flush();
		return writer.toString().replace("\n", "");
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
	
	private String String2ContentFBHTML(String image_url, String feedbackID, String leaf, String error_code,
			String id_learner, String id_task, String statementRef) throws IOException
	{
		if(image_url == null)
			return "";
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/contentFB1_1.mustache");
		
		ContentFB1_1 fbhtml = new ContentFB1_1(image_url, feedbackID, leaf, error_code, id_learner, id_task, statementRef);
		Map<String, Object> context = new HashMap<>();
	    context.put("fb_image", fbhtml);
	    
		StringWriter writer = new StringWriter();
		m.execute(writer, context).flush();
		return writer.toString().replace("\n", "");
	}

	private String String2GlossaryFBHTML(HashMap<String, String> glossaryMap, String feedbackID, String leaf, String error_code,
			String id_learner, String id_task, String statementRef) throws IOException {
		if (glossaryMap.size() == 0)
			return "";
		// Compiling the Mustache Template
		MustacheFactory mf = new DefaultMustacheFactory();
		Mustache m = mf.compile("mustache_template/contentFB1_1.mustache");
		
		List<GlossaryFB1_1> glossary_list = new ArrayList<GlossaryFB1_1>();
		Iterator<String> iterator = glossaryMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			GlossaryFB1_1 temp = new GlossaryFB1_1(glossaryMap.get(key), key, feedbackID, leaf, error_code, id_learner, id_task, statementRef);
			glossary_list.add(temp);
		}
		Map<String, Object> context = new HashMap<>();
		context.put("glossary", glossary_list);
		
		StringWriter writer = new StringWriter();
		m.execute(writer, context).flush();
		return writer.toString().replace("\n", "");
	}
	
	private String String2GlossaryFBHTML(HashMap<String, String> glossaryMap) throws IOException {
		if (glossaryMap.size() == 0)
			return "";
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
		return taskFamily;
	}

	public String getIdFeedback() {
		return idFb;
	}

	/**
	 * @return the decision mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the decision mode to define
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the reward
	 */
	public Double getReward() {
		return reward;
	}

	/**
	 * @param reward the reward value to define
	 */
	public void setReward(Double reward) {
		this.reward = reward;
	}

	public void setModifiedState(IState modifiedState) {
		this.modifiedState = modifiedState.toString();
	}

	public String getModifiedState() {
		return this.modifiedState;
	}

	public void setModifiedQvalues(List<IValue> modifiedQvalues) {
		this.qvalues = new TreeMap<>();

		for (IValue iValue : modifiedQvalues) {
			this.qvalues.put(iValue.myAction().getId(), iValue.getValue());
		}
	}

	public Map<String, Double> getModifiedQvalues() {
		return this.qvalues;
	}

	public String getIdTask() {
		return idTask;
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public double getSuccessScore() {
		return successScore;
	}

	public boolean isCloseTask() {
		return closeTask;
	}

	public String getCloseTaskReason() {
		return closeTaskReason;
	}
}

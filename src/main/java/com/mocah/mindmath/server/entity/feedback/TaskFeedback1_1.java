package com.mocah.mindmath.server.entity.feedback;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.task.Log;

/**
 * @author Yan Wang
 * @since 09/2020
 */

@Entity
public class TaskFeedback1_1 extends AbstractJsonData{
	
	private final String id_learner;
	
	private final String id_task;
	
	private final String domain;

	private final String generator;

	private final String taskFamily;

	private final boolean correctAnswer;

	private final String codeError;

	private final String activityMode;
	
	private final int maxFb;
	
	@OneToMany(cascade = CascadeType.ALL)
	private final List<Log> logs;
	
	private final String feedback_id;
	
	private final String leaf;
	
	private final String error_type;
	
	@Column(length=10000)
	private final String motivationalElementFb;
	
	@Column(length=10000)
	private final String contentFb;
	
	@Column(length=10000)
	private final String glossaryFb;
	
	private final String trigger;
	
	private final boolean success;
	
	private final boolean completion;
	
	private final double successScore;
	
	private final boolean closeTask;
	
	public TaskFeedback1_1() {
		this("", "", null, null, null, false, null, null, 4, new ArrayList<Log>(), null, null, null, null, null, null, null, false, false, 0, false);
	}

	public TaskFeedback1_1(String id_learner, String id_task, String domain, String generator, String taskFamily,
			boolean correctAnswer, String codeError, String activityMode, int maxFb, List<Log> logs,
			String feedback_id, String leaf, String error_type, String motivationalElementFb,
			String contentFb, String glossaryFb, String trigger, boolean success, boolean completion,
			double successScore, boolean closeTask) {
		super();
		this.id_learner = id_learner;
		this.id_task = id_task;
		this.domain = domain;
		this.generator = generator;
		this.taskFamily = taskFamily;
		this.correctAnswer = correctAnswer;
		this.codeError = codeError;
		this.activityMode = activityMode;
		this.maxFb = maxFb;
		this.logs = logs;
		this.feedback_id = feedback_id;
		this.leaf = leaf;
		this.error_type = error_type;
		this.motivationalElementFb = motivationalElementFb;
		this.contentFb = contentFb;
		this.glossaryFb = glossaryFb;
		this.trigger = trigger;
		this.success = success;
		this.completion = completion;
		this.successScore = successScore;
		this.closeTask = closeTask;
	}

	public String getId_learner() {
		return id_learner;
	}

	public String getDomain() {
		return domain;
	}

	public String getGenerator() {
		return generator;
	}

	public String getTaskFamily() {
		return taskFamily;
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public String getCodeError() {
		return codeError;
	}

	public String getActivityMode() {
		return activityMode;
	}

	public String getFeedback_id() {
		return feedback_id;
	}

	public String getLeaf() {
		return leaf;
	}

	public String getError_type() {
		return error_type;
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

	public String getTrigger() {
		return trigger;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isCompletion() {
		return completion;
	}

	public String getId_task() {
		return id_task;
	}

	public double getSuccessScore() {
		return successScore;
	}

	public boolean isCloseTask() {
		return closeTask;
	}

	public Integer getMaxFb() {
		return maxFb;
	}
}

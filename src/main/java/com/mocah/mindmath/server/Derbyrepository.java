package com.mocah.mindmath.server;

import org.springframework.data.repository.CrudRepository;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.task.Task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

public interface Derbyrepository extends CrudRepository <AbstractJsonData, String> {

	@Query("select t from Task t where t.id = (select max(t.id) from Task t)")
	Task getPreviousTask();
	
	@Query("select t from Task t")
	List<Task> getAllTask();
	
	@Query("select f from FeedbackContent f")
	List<FeedbackContent> getAllFeedbackContent();
}

package com.mocah.mindmath.server;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
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
	
	@Query("select f from FeedbackContent f where f.feedbackName = :feedbackName")
	FeedbackContent getFeedbackContent(@Param("feedbackName") String feedbackName);
	
	@Query("select g from Glossaire g where g.glossaire_name = :glossaire_name")
	Glossaire getGlossaire(@Param("glossaire_name") String glossaire_name);
	
	@Query("select m from Motivation m where m.motivation_leaf = :motivation_leaf")
	List<Motivation> getMotivation(@Param("motivation_leaf") String motivation_leaf);
}

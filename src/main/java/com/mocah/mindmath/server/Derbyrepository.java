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
	
	@Query("select g from Glossaire g")
	List<Glossaire> getAllGlossaire();
	
	@Query("select m from Motivation m")
	List<Motivation> getAllMotivation();
	
	@Query("select f from FeedbackContent f where f.feedbackID = :feedbackID and f.motivation_leaf = :motivation_leaf")
	FeedbackContent getFeedbackContent(@Param("feedbackID") String feedbackID, @Param("motivation_leaf") String motivation_leaf);
	
	@Query("select g from Glossaire g where g.glossaireID = :glossaireID")
	Glossaire getGlossaire(@Param("glossaireID") String glossaireID);
	
	@Query("select m from Motivation m where m.motivation_leaf = :motivation_leaf")
	List<Motivation> getMotivation(@Param("motivation_leaf") String motivation_leaf);
}

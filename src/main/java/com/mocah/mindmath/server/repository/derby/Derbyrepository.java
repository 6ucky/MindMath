package com.mocah.mindmath.server.repository.derby;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.mocah.mindmath.server.entity.AbstractJsonData;
import com.mocah.mindmath.server.entity.feedback.TaskFeedback1_1;
import com.mocah.mindmath.server.entity.feedbackContent.FeedbackContent;
import com.mocah.mindmath.server.entity.feedbackContent.Glossaire;
import com.mocah.mindmath.server.entity.feedbackContent.Motivation;
import com.mocah.mindmath.server.entity.task.Task;

/**
 * @author Yan Wang
 * @since 21/02/2020
 */

public interface Derbyrepository extends CrudRepository<AbstractJsonData, String> {

	@Query("select t from Task t where t.id = (select max(t.id) from Task t)")
	Task getPreviousTask();

	@Query("select t from Task t where t.id = (select max(t.id) from Task t where t.sensors.id_learner = :id_learner)")
	Task getPreviousTask(@Param("id_learner") String id_learner);

	@Query("select t from Task t")
	List<Task> getAllTask();
	
	@Query("select t from TaskFeedback1_1 t")
	List<TaskFeedback1_1> getAllTaskFeedback1_1();
	
	//calculate maxFb
	@Query("select t from TaskFeedback1_1 t where t.id_learner = :id_learner and t.id_task = :id_task")
	List<TaskFeedback1_1> getTaskFeedback1_1(
			@Param("id_learner") String id_learner,
			@Param("id_task") String id_task);
	
	//getNbSolveTry(Task task) 
	@Query("select t from TaskFeedback1_1 t where t.id_learner = :id_learner and t.id_task = :id_task and t.trigger = :trigger")
	List<TaskFeedback1_1> getTaskFeedback1_1(
			@Param("id_learner") String id_learner,
			@Param("id_task") String id_task,
			@Param("trigger") String trigger);
	
	//errorStabilityForLearner(Task task)
	@Query("select t from TaskFeedback1_1 t where t.id_learner = :id_learner and t.taskFamily = :taskFamily and t.trigger = :trigger and t.correctAnswer = :correctAnswer")
	List<TaskFeedback1_1> getTaskFeedback1_1(
			@Param("id_learner") String id_learner,
			@Param("taskFamily") String taskFamily,
			@Param("trigger") String trigger,
			@Param("correctAnswer") boolean correctAnswer);
	
	//mostStabErrorForLearners(Task task)
	@Query("select t from TaskFeedback1_1 t where t.taskFamily = :taskFamily and t.trigger = :trigger and t.correctAnswer = :correctAnswer")
	List<TaskFeedback1_1> getTaskFeedback1_1(
			@Param("taskFamily") String taskFamily,
			@Param("trigger") String trigger,
			@Param("correctAnswer") boolean correctAnswer);
	
	@Query("select t from TaskFeedback1_1 t where t.successScore = (select min(t.successScore) from TaskFeedback1_1 t where t.id_learner = :id_learner and t.id_task = :id_task) and t.id_learner = :id_learner and t.id_task = :id_task")
	TaskFeedback1_1 getPreviousTaskFeedback1_1(@Param("id_learner") String id_learner, @Param("id_task") String id_task);

	@Query("select f from FeedbackContent f")
	List<FeedbackContent> getAllFeedbackContent();

	@Query("select g from Glossaire g")
	List<Glossaire> getAllGlossaire();

	@Query("select m from Motivation m")
	List<Motivation> getAllMotivation();

	@Query("select f from FeedbackContent f where f.feedbackID = :feedbackID and f.motivation_leaf = :motivation_leaf")
	FeedbackContent getFeedbackContent(@Param("feedbackID") String feedbackID,
			@Param("motivation_leaf") String motivation_leaf);

	@Query("select g from Glossaire g where g.glossaireID = :glossaireID")
	Glossaire getGlossaire(@Param("glossaireID") String glossaireID);

	@Query("select m from Motivation m where m.motivation_leaf = :motivation_leaf")
	List<Motivation> getMotivation(@Param("motivation_leaf") String motivation_leaf);
}

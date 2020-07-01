package com.mocah.mindmath.server.cabri;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

public interface Taskrepository extends CrudRepository <Task, String> {

	@Query("select t from Task t where t.id = (select max(t.id) from Task t)")
	Task getPreviousTask();
}

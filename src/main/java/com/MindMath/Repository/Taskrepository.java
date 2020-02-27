package com.MindMath.Repository;

import org.springframework.data.repository.CrudRepository;

import com.MindMath.Server.cabri.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

public interface Taskrepository extends CrudRepository <Task, String> {

}

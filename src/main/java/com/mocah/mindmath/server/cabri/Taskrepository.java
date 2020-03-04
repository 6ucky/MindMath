package com.mocah.mindmath.server.cabri;

import org.springframework.data.repository.CrudRepository;

import com.mocah.mindmath.server.cabri.jsondata.Task;

/**
 * @author	Yan Wang
 * @since	21/02/2020
 */

public interface Taskrepository extends CrudRepository <Task, String> {

}

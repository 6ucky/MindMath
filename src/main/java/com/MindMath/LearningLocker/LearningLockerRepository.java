package com.MindMath.LearningLocker;

import java.util.Optional;

public interface LearningLockerRepository <T, ID> {

	public boolean existsById(ID id);
	
	public void save(T task);
	
	public Optional<T> findById(ID id);
	
	public Iterable<T> findAll();
	
	public void deleteAll();
	
}

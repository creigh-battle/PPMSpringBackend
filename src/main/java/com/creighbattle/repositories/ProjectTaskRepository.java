package com.creighbattle.repositories;

import com.creighbattle.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;

public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {
}

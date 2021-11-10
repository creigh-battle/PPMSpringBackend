package com.creighbattle.repositories;

import com.creighbattle.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

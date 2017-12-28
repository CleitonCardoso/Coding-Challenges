package com.sisteminha.repositories;

import org.springframework.data.repository.CrudRepository;

import com.sisteminha.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);

	User findByUsernameAndPasswordAndActiveIsTrue(String username, String password);
}

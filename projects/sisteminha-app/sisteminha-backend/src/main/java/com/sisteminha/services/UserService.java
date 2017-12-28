package com.sisteminha.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sisteminha.entities.User;
import com.sisteminha.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findByUserNameAndPassword(String username, String password) {
		return userRepository.findByUsernameAndPasswordAndActiveIsTrue( username, password );
	}

	public User save(User user) {
		return userRepository.save( user );
	}

	public User find(Long id) {
		return userRepository.findOne( id );
	}

	public void delete(Long id, String loggedUsername) throws Exception {
		User user = find( id );
		if (!loggedUsername.equals( user.getUsername() ))
			userRepository.delete( id );
		else
			throw new Exception( "Não é possível excluir um usuário logado" );
	}

	public Iterable<User> list() {
		return userRepository.findAll();
	}

}

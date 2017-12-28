package com.sisteminha.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sisteminha.entities.User;
import com.sisteminha.services.UserService;

@RequestMapping(path = "user")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody User user) {
		userService.save(user);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody User user) {
		userService.save(user);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public User find(@PathVariable("id") Long id) {
		return userService.find(id);
	}

	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public void delete(@PathVariable("id") Long id, Principal principal) throws Exception {
		userService.delete(id, principal.getName());
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<User> findAll() {
		return userService.list();
	}
}

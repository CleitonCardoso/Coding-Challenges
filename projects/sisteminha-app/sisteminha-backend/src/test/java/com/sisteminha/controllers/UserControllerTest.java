package com.sisteminha.controllers;

import java.security.Principal;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sisteminha.entities.User;
import com.sisteminha.entities.User.UserBuilder;
import com.sisteminha.repositories.UserRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserControllerTest {

	@Autowired
	private UserController userController;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void save() {
		User user = User.builder()
				.username( "user1" )
				.password( "password" )
				.active( true )
				.build();

		User userSavedByController = userController.save( user );

		User userFound = userRepository.findOne( userSavedByController.getId() );

		Assert.assertThat( userFound.getId(), Matchers.equalTo( userSavedByController.getId() ) );
		Assert.assertThat( userFound.getUsername(), Matchers.equalTo( user.getUsername() ) );
		Assert.assertThat( userFound.getPassword(), Matchers.equalTo( user.getPassword() ) );
		Assert.assertThat( userFound.isActive(), Matchers.equalTo( user.isActive() ) );
	}

	@Test
	public void find() {
		User user = User.builder()
				.username( "user 2" )
				.password( "pass" )
				.build();

		User userSavedByRepository = userRepository.save( user );

		User userFound = userController.find( userSavedByRepository.getId() );

		Assert.assertThat( userFound.getId(), Matchers.equalTo( userSavedByRepository.getId() ) );
		Assert.assertThat( userFound.getUsername(), Matchers.equalTo( user.getUsername() ) );
		Assert.assertThat( userFound.getPassword(), Matchers.equalTo( user.getPassword() ) );
		Assert.assertThat( userFound.isActive(), Matchers.equalTo( user.isActive() ) );
	}

	@Test
	public void update() {
		User userActive = User.builder()
				.active( true )
				.password( "pass" )
				.username( "user" )
				.build();

		User userSaved = userRepository.save( userActive );

		userSaved.setActive( false );

		userController.update( userSaved );

		User userFound = userController.find( userSaved.getId() );

		Assert.assertThat( userFound.getId(), Matchers.equalTo( userSaved.getId() ) );
		Assert.assertThat( userFound.isActive(), Matchers.equalTo( false ) );
		Assert.assertThat( userFound.getPassword(), Matchers.equalTo( userSaved.getPassword() ) );
		Assert.assertThat( userFound.getUsername(), Matchers.equalTo( userSaved.getUsername() ) );
	}

	@Test
	public void delete() throws Exception {
		User user = User.builder()
				.username( "user" )
				.password( "user" )
				.active( true )
				.build();

		User userSaved = userRepository.save( user );

		userController.delete( userSaved.getId(), new Principal() {
			@Override
			public String getName() {
				return "admin";
			}
		} );

		User UserFound = userRepository.findOne( userSaved.getId() );

		Assert.assertThat( UserFound, Matchers.nullValue() );
	}

	@Test(expected = Exception.class)
	public void throwsExceptionWhenDeletingLoggedUser() throws Exception {
		String username = "user";

		User user = User.builder()
				.username( username )
				.password( "user" )
				.active( true )
				.build();

		User userSaved = userRepository.save( user );

		userController.delete( userSaved.getId(), new Principal() {
			@Override
			public String getName() {
				return username;
			}
		} );

		userRepository.findOne( userSaved.getId() );

	}

	@Test
	public void list() {
		UserBuilder userBuilder = User.builder().password( "password" ).active( true );

		userRepository.save( userBuilder.username( "User 01" ).build() );
		userRepository.save( userBuilder.username( "User 02" ).build() );

		Iterable<User> users = userController.list();

		Assert.assertThat( users.spliterator().getExactSizeIfKnown(), Matchers.equalTo( 2L ) );
	}

}

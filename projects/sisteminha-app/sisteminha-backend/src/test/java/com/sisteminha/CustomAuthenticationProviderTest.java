package com.sisteminha;

import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.sisteminha.entities.User;
import com.sisteminha.services.UserService;

public class CustomAuthenticationProviderTest {

	@InjectMocks
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Mock
	private UserService userService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks( this );
	}

	@Test
	public void authenticateSuccess() {
		String username = "user";
		String password = "pass";

		Mockito.when( userService.findByUserNameAndPassword( username, password ) )
				.thenReturn( User.builder().build() );

		Authentication authentication = customAuthenticationProvider
				.authenticate( getAuthenticationObject( username, password ) );

		Mockito.verify( userService ).findByUserNameAndPassword( username, password );
		Assert.assertThat( authentication.getClass(), Matchers.equalTo( UsernamePasswordAuthenticationToken.class ) );
		Assert.assertThat( authentication.getName(), Matchers.equalTo( username ) );
		Assert.assertThat( authentication.getCredentials(), Matchers.equalTo( password ) );
	}

	@Test
	public void authenticateFailure() {
		String username = "user";
		String password = "pass";

		Mockito.when( userService.findByUserNameAndPassword( username, password ) ).thenReturn( null );

		thrown.expect( BadCredentialsException.class );
		thrown.expectMessage( "External system authentication failed" );
		customAuthenticationProvider.authenticate( getAuthenticationObject( username, password ) );
	}

	private Authentication getAuthenticationObject(String username, String password) {
		return new Authentication() {

			private static final long serialVersionUID = 6155845715639803313L;

			@Override
			public String getName() {
				return username;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}

			@Override
			public Object getPrincipal() {
				return null;
			}

			@Override
			public Object getDetails() {
				return null;
			}

			@Override
			public Object getCredentials() {
				return password;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return null;
			}
		};
	}
}

package my.groupid.account;

import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

	@InjectMocks
	UserController userController;

	@InjectMocks
	private AccountService accountService = new AccountService();

	@Mock
	private AccountRepository accountRepositoryMock;

	@Mock
	private AccountService accountServiceMock;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldInitializeWithTwoDemoUsers() {
		// act
		accountService.initialize();
		// assert
		verify(accountRepositoryMock, times(15)).save(any(Account.class));
	}

	@Test
	public void shouldThrowExceptionWhenUserNotFound() {
		// arrange
		thrown.expect(UsernameNotFoundException.class);
		thrown.expectMessage("user not found");

		when(accountRepositoryMock.findOneByEmail("user@example.com")).thenReturn(null);
		// act
		accountService.loadUserByUsername("user@example.com");
	}

	@Test
	public void shouldReturnUserDetails() {
		// arrange
		Account demoUser = new Account("user@example.com", "demo", "ROLE_USER");
		when(accountRepositoryMock.findOneByEmail("user@example.com")).thenReturn(demoUser);

		// act
		UserDetails userDetails = accountService.loadUserByUsername("user@example.com");

		// assert
		assertThat(demoUser.getEmail()).isEqualTo(userDetails.getUsername());
		assertThat(demoUser.getPassword()).isEqualTo(userDetails.getPassword());
		assertThat(hasAuthority(userDetails, demoUser.getRole())).isTrue();
	}

	@Test
	public void shouldReturnAllAccounts() {
		//Set the expectations for getting all the user accounts 
		when(accountServiceMock.getUsers()).thenReturn(getAccounts());

		Assert.assertNotNull(userController.getUsers());
		
		//The number of accounts that the controller returns should be same as that we set in teh expectation.
		Assert.assertTrue(userController.getUsers().size() == getAccounts().size());
	}

	/*
	 * Test Accounts
	 */
	public List<Account> getAccounts() {
		List<Account> accountList = new ArrayList<Account>();

		accountList.add(new Account("admin", "admin", "ROLE_ADMIN"));

		accountList.add(new Account("user01", "demo", "ROLE_USER"));
		accountList.add(new Account("user06", "demo", "ROLE_USER"));
		accountList.add(new Account("user08", "demo", "ROLE_USER"));
		accountList.add(new Account("user05", "demo", "ROLE_USER"));
		accountList.add(new Account("user07", "demo", "ROLE_USER"));
		accountList.add(new Account("user16", "demo", "ROLE_USER"));
		accountList.add(new Account("user18", "demo", "ROLE_USER"));
		accountList.add(new Account("user15", "demo", "ROLE_USER"));
		accountList.add(new Account("user19", "demo", "ROLE_USER"));
		accountList.add(new Account("user09", "demo", "ROLE_USER"));
		accountList.add(new Account("user13", "demo", "ROLE_USER"));
		accountList.add(new Account("user04", "demo", "ROLE_USER"));
		accountList.add(new Account("user20", "demo", "ROLE_USER"));
		accountList.add(new Account("user02", "demo", "ROLE_USER"));

		return accountList;
	}

	private boolean hasAuthority(UserDetails userDetails, String role) {
		return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(isEqual(role));
	}
}

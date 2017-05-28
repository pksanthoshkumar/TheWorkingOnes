package my.groupid.account;

import java.util.Collections;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountService implements UserDetailsService {
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct	
	protected void initialize() {
		save(new Account("admin", "admin", "ROLE_ADMIN"));
		save(new Account("user01", "demo", "ROLE_USER"));
		save(new Account("user06", "demo", "ROLE_USER"));
		save(new Account("user08", "demo", "ROLE_USER"));
		save(new Account("user05", "demo", "ROLE_USER"));
		save(new Account("user07", "demo", "ROLE_USER"));
		save(new Account("user16", "demo", "ROLE_USER"));
		save(new Account("user18", "demo", "ROLE_USER"));
		save(new Account("user15", "demo", "ROLE_USER"));
		save(new Account("user19", "demo", "ROLE_USER"));
		save(new Account("user09", "demo", "ROLE_USER"));
		save(new Account("user13", "demo", "ROLE_USER"));
		save(new Account("user04", "demo", "ROLE_USER"));
		save(new Account("user20", "demo", "ROLE_USER"));
		save(new Account("user02", "demo", "ROLE_USER"));
	}

	@Transactional
	public Account save(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		return account;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findOneByEmail(username);
		if(account == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return createUser(account);
	}
	
	public void signin(Account account) {
		SecurityContextHolder.getContext().setAuthentication(authenticate(account));
	}
	
	private Authentication authenticate(Account account) {
		return new UsernamePasswordAuthenticationToken(createUser(account), null, Collections.singleton(createAuthority(account)));		
	}
	
	private User createUser(Account account) {
		return new User(account.getEmail(), account.getPassword(), Collections.singleton(createAuthority(account)));
	}

	private GrantedAuthority createAuthority(Account account) {
		return new SimpleGrantedAuthority(account.getRole());
	}

}

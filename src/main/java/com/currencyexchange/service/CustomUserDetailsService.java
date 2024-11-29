package com.currencyexchange.service;

import static com.currencyexchange.exception.ErrorCode.ACCOUNT_NOT_EXISTS;

import com.currencyexchange.model.Account;
import com.currencyexchange.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	public static final String USER_ROLE = "USER";
	private final AccountRepository accountRepository;

	public CustomUserDetailsService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByLogin(username)
				.orElseThrow(() -> new UsernameNotFoundException(ACCOUNT_NOT_EXISTS.getMessage()));

		return User.builder()
				.username(account.getLogin())
				.password(account.getPassword())
				.roles(USER_ROLE)
				.build();
	}
}


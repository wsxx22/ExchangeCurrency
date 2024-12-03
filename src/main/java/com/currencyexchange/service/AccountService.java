package com.currencyexchange.service;

import static com.currencyexchange.exception.ErrorCode.ACCOUNT_NOT_EXISTS;
import static com.currencyexchange.exception.ErrorCode.SOURCE_BALANCE_NOT_FOUND;

import com.currencyexchange.dto.AccountRequest;
import com.currencyexchange.dto.AccountResponse;
import com.currencyexchange.dto.BalanceDto;
import com.currencyexchange.dto.ExchangeRequest;
import com.currencyexchange.exception.ErrorCode;
import com.currencyexchange.exception.InsufficientFundsException;
import com.currencyexchange.exception.NotFoundException;
import com.currencyexchange.factory.AccountResponseFactory;
import com.currencyexchange.model.Account;
import com.currencyexchange.model.AccountBalance;
import com.currencyexchange.model.Currency;
import com.currencyexchange.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	private final Object lock = new Object();

	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public AccountResponse createAccount(AccountRequest request) {
		char[] rawPassword = request.getPassword();

		try {
			Account account = Account.builder()
					.login(request.getLogin())
					.password(passwordEncoder.encode(new String(rawPassword)))
					.firstName(request.getFirstName())
					.lastName(request.getLastName())
					.build();

			account.setBalances(
					List.of(AccountBalance.builder().account(account).currency(Currency.PLN).balance(request.getInitialBalance()).build()));

			return AccountResponseFactory.create(accountRepository.save(account));
		} finally {
			Arrays.fill(rawPassword, '\0');
		}

	}

	public AccountResponse getAccountDetails(UUID accountId) {
		Account account = accountRepository.findByIdWithBalances(accountId)
				.orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_EXISTS.getMessage()));

		List<BalanceDto> filteredBalances = account.getBalances()
				.stream()
				.map(balance -> BalanceDto.builder().currency(balance.getCurrency()).balance(balance.getBalance()).build())
				.collect(Collectors.toList());

		return AccountResponse.builder()
				.id(account.getId())
				.login(account.getLogin())
				.firstName(account.getFirstName())
				.lastName(account.getLastName())
				.balances(filteredBalances)
				.build();
	}

	public Account findById(UUID id) {
		return accountRepository.findByIdWithBalances(id).orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_EXISTS.getMessage()));
	}

	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public Account updateAccountBalances(Account account, ExchangeRequest exchangeRequest, BigDecimal exchangedAmount) {
		Currency sourceCurrency = exchangeRequest.getSourceCurrency();
		Currency targetCurrency = exchangeRequest.getTargetCurrency();

		synchronized (lock) {
			AccountBalance sourceBalance = account.getBalances()
					.stream()
					.filter(balance -> balance.getCurrency().equals(sourceCurrency))
					.findFirst()
					.orElseThrow(() -> new NotFoundException(SOURCE_BALANCE_NOT_FOUND.getMessage()));

			AccountBalance targetBalance = account.getBalances()
					.stream()
					.filter(balance -> balance.getCurrency().equals(targetCurrency))
					.findFirst()
					.orElse(AccountBalance.builder().account(account).currency(targetCurrency).balance(BigDecimal.ZERO).build());

			sourceBalance.setBalance(sourceBalance.getBalance().subtract(exchangeRequest.getAmount()));
			targetBalance.setBalance(targetBalance.getBalance().add(exchangedAmount));

			if (!account.getBalances().contains(targetBalance)) {
				account.getBalances().add(targetBalance);
			}
		}

		return accountRepository.save(account);
	}

	void validAccountBalance(Account account, ExchangeRequest exchangeRequest) {
		account.getBalances()
				.stream()
				.filter(balance -> balance.getCurrency().equals(exchangeRequest.getSourceCurrency()))
				.findFirst()
				.ifPresent(balance -> {
					if (balance.getBalance().compareTo(exchangeRequest.getAmount()) < 0) {
						throw new InsufficientFundsException(ErrorCode.INSUFFICIENT_FUNDS, exchangeRequest.getSourceCurrency());
					}
				});
	}

}

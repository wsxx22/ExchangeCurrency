package com.currencyexchange.factory;

import com.currencyexchange.dto.AccountResponse;
import com.currencyexchange.dto.BalanceDto;
import com.currencyexchange.model.Account;

import java.util.List;
import java.util.stream.Collectors;

public class AccountResponseFactory {

	public static AccountResponse create(Account account) {
		return AccountResponse.builder()
				.id(account.getId())
				.login(account.getLogin())
				.firstName(account.getFirstName())
				.lastName(account.getLastName())
				.balances(mapBalances(account))
				.build();
	}

	public static AccountResponse basic(Account account) {
		return AccountResponse.builder()
				.id(account.getId())
				.login(account.getLogin())
				.balances(mapBalances(account))
				.build();
	}

	private static List<BalanceDto> mapBalances(Account account) {
		return account.getBalances().stream()
				.map(balance -> BalanceDto.builder()
						.currency(balance.getCurrency())
						.balance(balance.getBalance())
						.build())
				.collect(Collectors.toList());
	}
}



package com.currencyexchange.exception;

public enum ErrorCode {
	ACCOUNT_NOT_EXISTS("Account not exists"),
	SOURCE_BALANCE_NOT_FOUND("No balance in source currency"),
	TARGET_BALANCE_NOT_FOUND("Target balance not found"),
	RATE_NOT_FOUND("The exchange rate is only supported from PLN"),
	UNSUPPORTED_CURRENCY_PAIR("Unsupported currency pair"),
	INSUFFICIENT_FUNDS("Insufficient funds for currency");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

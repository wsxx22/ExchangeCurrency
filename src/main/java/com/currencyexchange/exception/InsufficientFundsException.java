package com.currencyexchange.exception;

import com.currencyexchange.model.Currency;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(ErrorCode errorCode, Currency currency) {
		super(errorCode.getMessage() + ": " + currency);
	}

}

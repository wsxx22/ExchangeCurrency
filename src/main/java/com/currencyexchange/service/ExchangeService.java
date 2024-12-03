package com.currencyexchange.service;

import static com.currencyexchange.exception.ErrorCode.UNSUPPORTED_CURRENCY_PAIR;

import com.currencyexchange.dto.AccountResponse;
import com.currencyexchange.dto.ExchangeRequest;
import com.currencyexchange.dto.NbpResponse;
import com.currencyexchange.exception.ErrorCode;
import com.currencyexchange.exception.NotFoundException;
import com.currencyexchange.factory.AccountResponseFactory;
import com.currencyexchange.model.Account;
import com.currencyexchange.model.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

	private final ExchangeRateProvider rateProvider;
	private final ExchangeRateCache rateCache;

	private final AccountService accountService;

	public ExchangeService(ExchangeRateProvider rateProvider, ExchangeRateCache rateCache, AccountService accountService) {
		this.rateProvider = rateProvider;
		this.rateCache = rateCache;
		this.accountService = accountService;
	}

	public AccountResponse exchangeCurrency(ExchangeRequest exchangeRequest) {
		Account account = accountService.findById(exchangeRequest.getAccountId());
		accountService.validAccountBalance(account, exchangeRequest);

		Currency targetCurrency = exchangeRequest.getTargetCurrency();
		Currency sourceCurrency = exchangeRequest.getSourceCurrency();
		NbpResponse.Rate rate = fetchRate(sourceCurrency, targetCurrency);

		BigDecimal exchangedAmount = calculateExchange(sourceCurrency, targetCurrency, exchangeRequest.getAmount(), rate);

		return AccountResponseFactory.basic(accountService.updateAccountBalances(account, exchangeRequest, exchangedAmount));
	}

	private BigDecimal calculateExchange(Currency sourceCurrency, Currency targetCurrency, BigDecimal amount, NbpResponse.Rate rate) {
		if (Currency.PLN.equals(sourceCurrency)) {
			return amount.divide(rate.getAskRate(), 2, RoundingMode.HALF_UP);
		} else if (Currency.PLN.equals(targetCurrency)) {
			return amount.multiply(rate.getBidRate()).setScale(2, RoundingMode.HALF_UP);
		}
		throw new NotFoundException(UNSUPPORTED_CURRENCY_PAIR.getMessage());
	}

	private NbpResponse.Rate fetchRate(Currency sourceCurrency, Currency targetCurrency) {
		if (Currency.PLN.equals(sourceCurrency) || Currency.PLN.equals(targetCurrency)) {
			String key = Currency.PLN.equals(sourceCurrency) ? targetCurrency.name() : sourceCurrency.name();

			return rateCache.getRate(key).filter(rateCache::isRateValid).orElseGet(() -> {
				NbpResponse.Rate rate = rateProvider.fetchRate(key);
				rateCache.putRate(key, rate);
				return rate;
			});
		}

		throw new NotFoundException(ErrorCode.RATE_NOT_FOUND.getMessage());
	}

}

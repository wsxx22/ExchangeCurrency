package com.currencyexchange.service;

import com.currencyexchange.dto.NbpResponse;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateCache {

	public static final String EXCHANGE_RATES = "exchangeRates";
	private final CacheManager cacheManager;

	public ExchangeRateCache(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public Optional<NbpResponse.Rate> getRate(String currencyCode) {
		Cache cache = cacheManager.getCache(EXCHANGE_RATES);
		return Optional.ofNullable(cache.get(currencyCode, NbpResponse.Rate.class));
	}

	public void putRate(String currencyCode, NbpResponse.Rate rate) {
		cacheManager.getCache(EXCHANGE_RATES).put(currencyCode, rate);
	}

	public boolean isRateValid(NbpResponse.Rate rate) {
		return rate.getDate().equals(LocalDate.now());
	}

}

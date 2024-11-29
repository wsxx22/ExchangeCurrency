package com.currencyexchange.service;

import com.currencyexchange.config.NbpApiConfig;
import com.currencyexchange.dto.NbpResponse;
import com.currencyexchange.exception.NotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExchangeRateProvider {

	private final WebClient webClient;

	public ExchangeRateProvider(WebClient.Builder webClientBuilder, NbpApiConfig nbpApiConfig) {
		this.webClient = webClientBuilder.baseUrl(nbpApiConfig.getBaseUrl()).build();
	}

	public NbpResponse.Rate fetchRate(String currencyCode) {
		return Optional.ofNullable(webClient.get().uri("/{code}/", currencyCode).retrieve().bodyToMono(NbpResponse.class).block())
				.map(NbpResponse::getRates)
				.map(Collection::stream)
				.flatMap(Stream::findFirst)
				.orElseThrow(() -> new NotFoundException(String.format("Exchange rate for %s not found", currencyCode)));
	}

}

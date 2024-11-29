package com.currencyexchange.controller;

import com.currencyexchange.dto.AccountResponse;
import com.currencyexchange.dto.ExchangeRequest;
import com.currencyexchange.service.ExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@PostMapping
	public ResponseEntity<AccountResponse> exchangeCurrency(@Valid @RequestBody ExchangeRequest exchangeRequest) {
		AccountResponse response = exchangeService.exchangeCurrency(exchangeRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}


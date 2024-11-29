package com.currencyexchange.controller;

import com.currencyexchange.dto.AccountRequest;
import com.currencyexchange.dto.AccountResponse;
import com.currencyexchange.service.AccountService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping("register")
	public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
		AccountResponse response = accountService.createAccount(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<AccountResponse> getAccountDetails(@PathVariable UUID accountId) {
		AccountResponse response = accountService.getAccountDetails(accountId);
		return ResponseEntity.ok(response);
	}

}

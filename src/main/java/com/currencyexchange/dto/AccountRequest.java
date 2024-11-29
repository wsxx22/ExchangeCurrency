package com.currencyexchange.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

	@Pattern(regexp = "^[a-zA-Z]{1,}$", message = "First name must contain only letters and at least one character")
	private String firstName;

	@Pattern(regexp = "^[a-zA-Z]{1,}$", message = "Last name must contain only letters and at least one character")
	private String lastName;

	@Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "Login must contain only letters and numbers, and be between 4 and 20 characters")
	private String login;

	@NotNull(message = "The password must not be empty")
	private char[] password;

	@PositiveOrZero
	private BigDecimal initialBalance;

}


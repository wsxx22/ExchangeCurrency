package com.currencyexchange.dto;

import com.currencyexchange.model.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
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
public class ExchangeRequest {

	@NotNull
	private UUID accountId;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private Currency sourceCurrency;

	@NotNull
	private Currency targetCurrency;

}

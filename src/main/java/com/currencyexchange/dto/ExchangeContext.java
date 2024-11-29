package com.currencyexchange.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExchangeContext {

	private final BigDecimal amount;

	private final NbpResponse.Rate rate;
}
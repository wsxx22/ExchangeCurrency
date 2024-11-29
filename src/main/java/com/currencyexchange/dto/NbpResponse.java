package com.currencyexchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NbpResponse {

	private String code;

	private List<Rate> rates;

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class Rate {

		@JsonProperty("effectiveDate")
		private LocalDate date;

		@JsonProperty("bid")
		private BigDecimal bidRate;

		@JsonProperty("ask")
		private BigDecimal askRate;

	}

}


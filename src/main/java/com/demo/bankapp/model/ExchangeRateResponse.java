package com.demo.bankapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateResponse {
    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;
}

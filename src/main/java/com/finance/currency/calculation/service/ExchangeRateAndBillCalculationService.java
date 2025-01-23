package com.finance.currency.calculation.service;

import com.finance.currency.calculation.model.Bill;
import com.finance.currency.calculation.response.CurrencyRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExchangeRateAndBillCalculationService {

	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateAndBillCalculationService.class);

	@Value("${api.url}")
    public String apiUrl;

	@Value("${api.key}")
    public String apiKey;



	@PreAuthorize("hasRole('USER')")
	public double calculateTotalPayableAmount(Bill bill) {
		double exchangeRate = fetchExchangeRate(bill.originalCurrency(),bill.targetCurrency());
		return discountService.applyDiscounts(bill.items(),bill.userType(),bill.originalCurrency(),bill.targetCurrency(),exchangeRate,bill.tenure());
	}

	@Autowired
	DiscountService discountService;
	@Cacheable(value = "exchangeRates", key = "#originalCurrency + '_' + #targetCurrency", unless = "#result == null || #result == 0")
	public double fetchExchangeRate(String originalCurrency, String targetCurrency) {
		String exchangeRateUrl = buildExchangeRateUrl(originalCurrency);

		logger.debug("Fetching exchange rate from URI: {} for {} to {}", exchangeRateUrl, originalCurrency, targetCurrency);

		WebClient webClient = WebClient.create();
		CurrencyRateResponse response = webClient.get()
				.uri(exchangeRateUrl)
				.retrieve()
				.bodyToMono(CurrencyRateResponse.class)
				.block();

		logger.debug("Currency exchange response: {}", response);

		return response != null && response.getRates() != null ? response.getRates().get(targetCurrency) : 0;
	}

	public String buildExchangeRateUrl(String originalCurrency) {
		if (apiUrl != null && apiKey != null) {
			return String.format("%s%s?apikey=%s", apiUrl, originalCurrency, apiKey);
		}
		return String.format("https://open.er-api.com/v6/latest/%s?apikey=your-api-key", originalCurrency);
	}
}

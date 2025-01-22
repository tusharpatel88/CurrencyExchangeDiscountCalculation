package com.finance.currency.calculation.service;

import com.finance.currency.calculation.model.Bill;
import com.finance.currency.calculation.model.Items;
import com.finance.currency.calculation.response.CurrencyRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ExchangeRateAndBillCalculationService {

	private static final Logger logger = LoggerFactory.getLogger(ExchangeRateAndBillCalculationService.class);

	@Value("${api.url}")
	private String apiUrl;

	@Value("${api.key}")
	private String apiKey;

	public double calculateDiscount(Bill bill) {
		double totalAmount = getTotalAmount(bill.items());
		double groceryAmount = getTotalAmountForGroceryItems(bill.items());

		double discountAmount = calculateNonGroceryDiscount(bill, totalAmount);
		double groceryDiscount = applyGroceryDiscount(groceryAmount);

		double additionalFivePercentDiscount = (discountAmount / 100) * 5;
		return (discountAmount + groceryDiscount) - additionalFivePercentDiscount;
	}

	private double calculateNonGroceryDiscount(Bill bill, double totalAmount) {
		if (bill.items().stream().anyMatch(item -> !item.category().equalsIgnoreCase("grocery"))) {
			return switch (bill.userType().toLowerCase()) {
				case "employee" -> totalAmount * 0.30;
				case "affiliate" -> totalAmount * 0.10;
				case "oldcustomer" -> totalAmount * 0.05;
				default -> 0;
			};
		}
		return 0;
	}

	private double applyGroceryDiscount(double groceryAmount) {
		return groceryAmount - (groceryAmount * 0.05);
	}

	private double getTotalAmount(List<Items> items) {
		return items.stream()
				.filter(item -> !item.category().equalsIgnoreCase("grocery"))
				.mapToDouble(Items::amount)
				.sum();
	}

	private double getTotalAmountForGroceryItems(List<Items> items) {
		return items.stream()
				.filter(item -> item.category().equalsIgnoreCase("grocery"))
				.mapToDouble(Items::amount)
				.sum();
	}

	@PreAuthorize("hasRole('USER')")
	public double calculateTotalPayableAmount(Bill bill) {
		double exchangeRate = fetchExchangeRate(bill.originalCurrency(),bill.targetCurrency());
		return discountService.applyDiscounts(bill.items(),bill.userType(),bill.originalCurrency(),bill.targetCurrency(),exchangeRate);
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

	private String buildExchangeRateUrl(String originalCurrency) {
		if (apiUrl != null && apiKey != null) {
			return String.format("%s%s?apikey=%s", apiUrl, originalCurrency, apiKey);
		}
		return String.format("https://open.er-api.com/v6/latest/%s?apikey=your-api-key", originalCurrency);
	}
}

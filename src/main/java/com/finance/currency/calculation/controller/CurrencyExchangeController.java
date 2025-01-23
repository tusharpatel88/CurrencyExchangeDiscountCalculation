package com.finance.currency.calculation.controller;

import com.finance.currency.calculation.model.ApiResponse;
import com.finance.currency.calculation.model.Bill;
import com.finance.currency.calculation.service.ExchangeRateAndBillCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling currency exchange and discount calculation requests.
 */
@RestController
@RequestMapping("/api")
public class CurrencyExchangeController {

	private final ExchangeRateAndBillCalculationService exchangeRateService;

	@Autowired
	public CurrencyExchangeController(ExchangeRateAndBillCalculationService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	/**
	 * Endpoint to calculate the final payable amount after applying discounts and currency conversion.
	 *
	 * @param bill the bill details including items, categories, and currencies.
	 * @return the final payable amount after all calculations.
	 */
	@PostMapping("/calculate")
	public ResponseEntity<ApiResponse> calculatePayableAmount(@RequestBody Bill bill) {
		double finalAmount = exchangeRateService.calculateTotalPayableAmount(bill);
		return ResponseEntity.ok(new ApiResponse(finalAmount, bill.targetCurrency()));
	}
}

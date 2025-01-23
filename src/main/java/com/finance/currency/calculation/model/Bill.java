package com.finance.currency.calculation.model;

import java.util.List;

public record Bill(String userType, // employee, affiliate, customer, tenure
		List<Items> items, String originalCurrency, String targetCurrency,int
						   tenure) {
}

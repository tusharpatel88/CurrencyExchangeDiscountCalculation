package com.finance.currency.calculation.service;

import com.finance.currency.calculation.model.Items;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscountService {

    private static final int EMPLOYEE_DISCOUNT = 30;
    private static final int AFFILIATE_DISCOUNT = 10;
    private static final int LOYAL_CUSTOMER_DISCOUNT = 5;
    private static final int BILL_DISCOUNT_PERCENTAGE = 5;
    private static final int LOYAL_CUSTOMER_YEARS_THRESHOLD = 2;

    private static final String EMPLOYEE_USER_TYPE = "employee";
    private static final String AFFILIATE_USER_TYPE = "affiliate";
    private static final String GROCERIES_CATEGORY = "Groceries";

    public double applyDiscounts(List<Items> items, String userType, String originalCurrency, String targetCurrency, double exchangeRate, int tenure) {
        double totalAmount = calculateTotalAmount(items);
        double discountPercentage = determineDiscountPercentage(userType, tenure);
        double billDiscount = calculateBillDiscount(totalAmount);
        double percentageDiscount = calculateNonGroceriesPercentageDiscount(items, discountPercentage);
        double totalAfterDiscount = totalAmount - billDiscount - percentageDiscount;
        return convertCurrency(totalAfterDiscount, exchangeRate);
    }

    public double calculateTotalAmount(List<Items> items) {
        return items.stream().mapToDouble(Items::amount).sum();
    }

    public double determineDiscountPercentage(String userType, int tenure) {
        if (userType.equalsIgnoreCase(EMPLOYEE_USER_TYPE)) {
            return EMPLOYEE_DISCOUNT;
        } else if (userType.equalsIgnoreCase(AFFILIATE_USER_TYPE)) {
            return AFFILIATE_DISCOUNT;
        } else if (tenure > LOYAL_CUSTOMER_YEARS_THRESHOLD) {
            return LOYAL_CUSTOMER_DISCOUNT;
        }
        return 0;
    }

    public double calculateBillDiscount(double totalAmount) {
        return (int)(totalAmount / 100) * BILL_DISCOUNT_PERCENTAGE;
    }

    public double calculateNonGroceriesPercentageDiscount(List<Items> items, double discountPercentage) {
        return items.stream()
                .filter(item -> !item.category().equalsIgnoreCase(GROCERIES_CATEGORY))
                .mapToDouble(item -> item.amount() * (discountPercentage / 100))
                .sum();
    }

    public double convertCurrency(double amount, double exchangeRate) {
        return amount * exchangeRate;
    }
}

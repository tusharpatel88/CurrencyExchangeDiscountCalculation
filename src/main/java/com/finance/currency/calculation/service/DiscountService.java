package com.finance.currency.calculation.service;

import com.finance.currency.calculation.model.Items;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscountService {

    public double applyDiscounts(List<Items> items, String userType, String originalCurrency, String targetCurrency, double exchangeRate) {
        // Step 1: Calculate total amount for all items
        double totalAmount = items.stream().mapToDouble(Items::amount).sum();

        // Step 2: Apply percentage-based discount based on user type
        double discountPercentage = 0;
        if (userType.equalsIgnoreCase("employee")) {
            discountPercentage = 30;
        } else if (userType.equalsIgnoreCase("affiliate")) {
            discountPercentage = 10;
        } else if (userType.equalsIgnoreCase("loyal_customer")) {
            discountPercentage = 5;
        }

        // Step 3: Apply $5 per $100 rule discount (flat discount)
        double billDiscount = (totalAmount / 100) * 5;

        // Step 4: Apply the percentage discount (only if not on groceries)
        double percentageDiscount = 0;
        if (discountPercentage > 0 && !containsGroceries(items)) {
            percentageDiscount = (totalAmount * discountPercentage) / 100;
        }

        // Step 5: Choose the best discount (either percentage or flat $5 per $100)
        double bestDiscount = Math.max(percentageDiscount, billDiscount);

        // Step 6: Calculate total after applying the best discount
        double totalAfterDiscount = totalAmount - bestDiscount;

        // Step 7: Convert the total amount to the target currency
        double totalInTargetCurrency = totalAfterDiscount * exchangeRate;

        return totalInTargetCurrency;
    }

    // Helper method to check if there are groceries in the list
    private boolean containsGroceries(List<Items> items) {
        return items.stream().anyMatch(item -> "Groceries".equalsIgnoreCase(item.category()));
    }
}

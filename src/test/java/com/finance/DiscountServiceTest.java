package com.finance;

import com.finance.currency.calculation.model.Items;
import com.finance.currency.calculation.service.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {

    @InjectMocks
    private DiscountService discountService;

    private List<Items> items;

    @BeforeEach
    void setUp() {
        items = List.of(
                new Items("Laptop", 1200.0, "Electronics"),
                new Items("Headphones", 150.0, "Electronics"),
                new Items("Apple", 5.0, "Groceries"),
                new Items("Banana", 3.0, "Groceries")
        );
    }

    @Test
    void testApplyDiscountsForEmployee() {
        double exchangeRate = 0.85;
        double result = discountService.applyDiscounts(items, "employee", "USD", "EUR", exchangeRate, 1);

        double totalAmount = 1200.0 + 150.0 + 5.0 + 3.0;
        double billDiscount = (int)(totalAmount / 100) * 5;
        double percentageDiscount = (1200.0 + 150.0) * 0.30;  // 30% on electronics
        double totalAfterDiscount = totalAmount - billDiscount - percentageDiscount;
        double expectedConvertedAmount = totalAfterDiscount * exchangeRate;

        assertEquals(expectedConvertedAmount, result, 0.001);
    }

    @Test
    void testApplyDiscountsForAffiliate() {
        double exchangeRate = 0.85;
        double result = discountService.applyDiscounts(items, "affiliate", "USD", "EUR", exchangeRate, 1);

        double totalAmount = 1200.0 + 150.0 + 5.0 + 3.0;
        double billDiscount = (int)(totalAmount / 100) * 5;
        double percentageDiscount = (1200.0 + 150.0) * 0.10;  // 10% on electronics
        double totalAfterDiscount = totalAmount - billDiscount - percentageDiscount;
        double expectedConvertedAmount = totalAfterDiscount * exchangeRate;

        assertEquals(expectedConvertedAmount, result, 0.001);
    }

    @Test
    void testApplyDiscountsForLoyalCustomer() {
        double exchangeRate = 0.85;
        double result = discountService.applyDiscounts(items, "regular", "USD", "EUR", exchangeRate, 3);

        double totalAmount = 1200.0 + 150.0 + 5.0 + 3.0;
        double billDiscount = (int)(totalAmount / 100) * 5;
        double percentageDiscount = (1200.0 + 150.0) * 0.05;  // 5% for loyal customers
        double totalAfterDiscount = totalAmount - billDiscount - percentageDiscount;
        double expectedConvertedAmount = totalAfterDiscount * exchangeRate;

        assertEquals(expectedConvertedAmount, result, 0.001);
    }



    @Test
    void testCalculateTotalAmount() {
        double result = discountService.calculateTotalAmount(items);
        double expectedTotalAmount = 1200.0 + 150.0 + 5.0 + 3.0;
        assertEquals(expectedTotalAmount, result, 0.001);
    }

    @Test
    void testDetermineDiscountPercentage() {
        assertEquals(30, discountService.determineDiscountPercentage("employee", 1));
        assertEquals(10, discountService.determineDiscountPercentage("affiliate", 1));
        assertEquals(5, discountService.determineDiscountPercentage("regular", 3));
        assertEquals(0, discountService.determineDiscountPercentage("regular", 1));
    }

    @Test
    void testCalculateBillDiscount() {
        assertEquals(50.0, discountService.calculateBillDiscount(1000.0), 0.001);
    }

    @Test
    void testCalculateNonGroceriesPercentageDiscount() {
        double result = discountService.calculateNonGroceriesPercentageDiscount(items, 10);
        double expectedDiscount = (1200.0 + 150.0) * 0.10;
        assertEquals(expectedDiscount, result, 0.001);
    }

    @Test
    void testConvertCurrency() {
        double result = discountService.convertCurrency(1000.0, 0.85);
        assertEquals(850.0, result, 0.001);
    }
}
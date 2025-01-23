package com.finance;

import com.finance.currency.calculation.controller.CurrencyExchangeController;
import com.finance.currency.calculation.model.ApiResponse;
import com.finance.currency.calculation.model.Bill;
import com.finance.currency.calculation.model.Items;
import com.finance.currency.calculation.service.ExchangeRateAndBillCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeControllerTest {

    @InjectMocks
    private CurrencyExchangeController currencyExchangeController;

    @Mock
    private ExchangeRateAndBillCalculationService exchangeRateService;

    private Bill bill;

    @BeforeEach
    void setUp() {
        bill = new Bill(
            "affiliate",
            List.of(
                new Items("Laptop", 1200.0, "Electronics"),
                new Items("Headphones", 150.0, "Electronics"),
                new Items("Apple", 5.0, "Groceries"),
                new Items("Banana", 3.0, "Groceries")
            ),
            "USD",
            "EUR",
            1
        );
    }

    @Test
    void testCalculatePayableAmount() {
        double finalAmount = 1000.0;
        when(exchangeRateService.calculateTotalPayableAmount(any(Bill.class))).thenReturn(finalAmount);

        ResponseEntity<ApiResponse> responseEntity = currencyExchangeController.calculatePayableAmount(bill);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(finalAmount, responseEntity.getBody().data());
        assertEquals(bill.targetCurrency(), responseEntity.getBody().currency());
    }
}

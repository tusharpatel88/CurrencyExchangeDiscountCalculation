package com.finance;

import com.finance.currency.calculation.model.Bill;
import com.finance.currency.calculation.model.Items;
import com.finance.currency.calculation.response.CurrencyRateResponse;
import com.finance.currency.calculation.service.DiscountService;
import com.finance.currency.calculation.service.ExchangeRateAndBillCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateAndBillCalculationServiceTest {

    @InjectMocks
    private ExchangeRateAndBillCalculationService exchangeRateService;

    @Mock
    private DiscountService discountService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

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

        exchangeRateService.apiUrl = "https://api.exchangerate-api.com/v6/latest/";
        exchangeRateService.apiKey =   "820e457a4d9883639fbff5c0";;
    }




    @Test
    void testBuildExchangeRateUrl() {
        String originalCurrency = "USD";
        exchangeRateService.apiKey = "820e457a4d9883639fbff5c0";
        String expectedUrl = "https://api.exchangerate-api.com/v6/latest/USD?apikey=820e457a4d9883639fbff5c0";

        String result = exchangeRateService.buildExchangeRateUrl(originalCurrency);

        assertEquals(expectedUrl, result);
    }
}

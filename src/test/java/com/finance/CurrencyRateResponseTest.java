package com.finance;

import static org.junit.jupiter.api.Assertions.*;

import com.finance.currency.calculation.response.CurrencyRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class CurrencyRateResponseTest {

    private CurrencyRateResponse currencyRateResponse;

    @BeforeEach
    public void setUp() {
        currencyRateResponse = new CurrencyRateResponse();
    }

    @Test
    public void testSetAndGetRates() {
        // Create a sample map to test the setter and getter
        Map<Object, Double> testRates = new HashMap<>();
        testRates.put("USD", 1.25);
        testRates.put("EUR", 0.85);
        
        // Set rates using the setter
        currencyRateResponse.setRates(testRates);
        
        // Get rates using the getter and assert they are the same
        Map<Object, Double> rates = currencyRateResponse.getRates();
        assertNotNull(rates, "Rates should not be null");
        assertEquals(2, rates.size(), "Rates map should contain two entries");
        assertEquals(1.25, rates.get("USD"), 0.01, "USD rate should be 1.25");
        assertEquals(0.85, rates.get("EUR"), 0.01, "EUR rate should be 0.85");
    }

    @Test
    public void testSetRatesWithNull() {
        // Set rates to null
        currencyRateResponse.setRates(null);
        
        // Get rates and assert it's null
        assertNull(currencyRateResponse.getRates(), "Rates should be null");
    }
}

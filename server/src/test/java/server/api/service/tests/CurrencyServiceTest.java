package server.api.service.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import server.api.services.CurrencyService;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CurrencyServiceTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for successful fetching of exchange rates.
     */
    @Test
    void fetchExchangeRatesGood() {
        LocalDate date = LocalDate.now();
        Map<String, Double> expectedRates = new HashMap<>();
        expectedRates.put("USD", 1.0);

        ResponseEntity<Map<String, Double>> responseEntity = new ResponseEntity<>(expectedRates, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(org.springframework.http.HttpMethod.GET), any(),
                any(Class.class))).thenReturn(responseEntity);

        ResponseEntity<Map<String, Double>> result = currencyService.fetchExchangeRates(date);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRates, result.getBody());
    }

    /**
     * Test for successful fetching of cached exchange rates.
     */
    @Test
    void fetchExchangeRatesCachedSuccess() {
        LocalDate date = LocalDate.now();
        Map<String, Double> expectedRates = new HashMap<>();
        expectedRates.put("USD", 1.0);
        String cacheFileName = "rates/" + date + ".txt";
        currencyService.cacheRates(cacheFileName, expectedRates);

        ResponseEntity<Map<String, Double>> result = currencyService.fetchExchangeRates(date);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRates, result.getBody());
    }

    /**
     * Test for successful currency conversion.
     */
    @Test
    void convertCurrencySuccess() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        String fromCurrency = "USD";
        String toCurrency = "EUR";
        double expectedConvertedAmount = 85.0;

        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 0.85);
        String cacheFileName = "rates/" + date + ".txt";
        currencyService.cacheRates(cacheFileName, exchangeRates);

        ResponseEntity<Double> result = currencyService.convertCurrency(amount, fromCurrency, toCurrency, date);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedConvertedAmount, result.getBody());
    }

    /**
     * Test for handling missing exchange rates while currency conversion.
     */
    @Test
    void convertCurrencyMissingExchangeRates() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        String fromCurrency = "USD";
        String toCurrency = "EUR";

        ResponseEntity<Double> result = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }

    /**
     * Test for handling invalid currency codes while currency conversion.
     */
    @Test
    void convertCurrencyInvalidCurrencyCodes() {
        LocalDate date = LocalDate.now();
        double amount = 100.0;
        String fromCurrency = "AAA";
        String toCurrency = "BBB";

        ResponseEntity<Double> result =
                new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
    @AfterAll
    public static void clean(){
        File file = new File("rates/"+LocalDate.now()+".txt");
        file.delete();
    }
}
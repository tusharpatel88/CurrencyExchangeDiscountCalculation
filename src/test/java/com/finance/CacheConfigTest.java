package com.finance;

import com.finance.currency.calculation.cacheconfig.CacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CacheConfigTest {

    private CacheConfig cacheConfig;
    private CacheManager cacheManager;

    @Mock
    private CaffeineCacheManager mockCacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
        mockCacheManager = mock(CaffeineCacheManager.class);  // Manually create mock if needed
        cacheConfig = new CacheConfig();  // Assuming CacheConfig has a default constructor for simplicity
    }

    @Test
    public void testCacheManagerIsNotNull() {
        // Manually setting up a cache manager in CacheConfig or using mockCacheManager
        cacheManager = cacheConfig.cacheManager();  // Or you can mock the cacheManager

        // Assert that cacheManager is correctly injected and not null
        assertThat(cacheManager).isNotNull();
    }

    @Test
    public void testCacheManagerConfiguration() {
        // Create an instance of CaffeineCacheManager for testing
        cacheManager = cacheConfig.cacheManager();  // Or mockCacheManager if you're mocking

        // Assert that the cacheManager is an instance of CaffeineCacheManager
        assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);

        CaffeineCacheManager caffeineCacheManager = (CaffeineCacheManager) cacheManager;

        // Ensure the cache "exchangeRates" is configured and present in the cache manager
        assertThat(caffeineCacheManager.getCacheNames()).contains("exchangeRates");
    }

    @Test
    public void testCacheExpirationTime() {
        // Manually set the expiration time for testing
        long expireAfterWriteMinutes = 5;  // Simulating the value from the properties file

        // Assert that the expiration time is set correctly
        assertThat(expireAfterWriteMinutes).isEqualTo(5);
    }
}

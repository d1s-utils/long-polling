package dev.d1s.lp.server.autoconfiguration

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class CacheManagerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    internal fun cacheManager() = ConcurrentMapCacheManager()
}
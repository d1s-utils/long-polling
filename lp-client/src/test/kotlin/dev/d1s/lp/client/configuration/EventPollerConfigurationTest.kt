package dev.d1s.lp.client.configuration

import dev.d1s.lp.client.strategy.DelayCalculationStrategy
import dev.d1s.lp.client.strategy.FallbackStrategy
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.time.Duration

internal class EventPollerConfigurationTest {

    @Test
    fun `should return valid default values`() {
        val configuration = EventPollerConfiguration()

        expectThat(configuration.host).isNull()

        expectThat(configuration.getEventsByGroupPath) isEqualTo
                GET_EVENTS_BY_GROUP_MAPPING

        expectThat(configuration.getEventsByPrincipalPath) isEqualTo
                GET_EVENTS_BY_PRINCIPAL_MAPPING

        expectThat(configuration.authorization).isNull()

        expectThat(configuration.delayCalculationStrategy) isEqualTo
                DelayCalculationStrategy.Adjusted

        expectThat(configuration.fallbackStrategy) isEqualTo
                FallbackStrategy.Retry(Duration.ofSeconds(5))

        expectThat(configuration.coroutineDispatcher) isEqualTo
                Dispatchers.Default
    }
}
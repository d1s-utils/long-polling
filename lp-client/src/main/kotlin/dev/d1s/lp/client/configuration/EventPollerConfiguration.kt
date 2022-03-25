package dev.d1s.lp.client.configuration

import dev.d1s.lp.client.strategy.FallbackStrategy
import dev.d1s.lp.client.strategy.DelayCalculationStrategy
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.Duration

public data class EventPollerConfiguration(
    var host: String? = null,
    var getEventsByGroupPath: String = GET_EVENTS_BY_GROUP_MAPPING,
    var getEventsByPrincipalPath: String = GET_EVENTS_BY_PRINCIPAL_MAPPING,
    var authorization: String? = null,
    var delayCalculationStrategy: DelayCalculationStrategy = DelayCalculationStrategy.Adjusted,
    var fallbackStrategy: FallbackStrategy = FallbackStrategy.Retry(Duration.ofSeconds(5)),
    var coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
)
package dev.d1s.lp.client.factory

import dev.d1s.lp.client.api.LongPollingClient
import dev.d1s.lp.client.configuration.EventPollerConfiguration

public fun longPollingClient(configuration: EventPollerConfiguration.() -> Unit): LongPollingClient =
    LongPollingClient().apply {
        this.updateConfiguration(configuration)
    }

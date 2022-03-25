package dev.d1s.lp.client.poller

import dev.d1s.lp.client.configuration.EventPollerConfiguration

internal interface EventPoller {

    suspend fun <T : Any> startPolling(
        group: String,
        principal: String?,
        type: Class<T>
    )

    fun block()

    fun updateConfiguration(configuration: EventPollerConfiguration.() -> Unit)

    fun stopPolling()
}
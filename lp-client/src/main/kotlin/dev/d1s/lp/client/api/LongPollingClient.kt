package dev.d1s.lp.client.api

import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.factory.eventPoller
import dev.d1s.lp.client.factory.longPollingEventListenerConfigurer
import dev.d1s.lp.client.factory.longPollingEventListenerRegistry
import dev.d1s.lp.client.listener.LongPollingEventListener

public class LongPollingClient {

    private val longPollingEventListenerRegistry = longPollingEventListenerRegistry()
    private val eventPoller = eventPoller(longPollingEventListenerRegistry)
    private val longPollingEventListenerConfigurer =
        longPollingEventListenerConfigurer(longPollingEventListenerRegistry, eventPoller)

    public suspend fun <T : Any> onEvent(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    ) {
        longPollingEventListenerConfigurer.configureListener(group, principal, type, listener)
    }

    public suspend inline fun <reified T : Any> onEvent(
        group: String,
        principal: String? = null,
        noinline listener: LongPollingEventListener<T>
    ) {
        this.onEvent(group, principal, T::class.java, listener)
    }

    public fun block() {
        eventPoller.block()
    }

    public fun updateConfiguration(configuration: EventPollerConfiguration.() -> Unit) {
        eventPoller.updateConfiguration(configuration)
    }

    public fun stopPolling() {
        eventPoller.stopPolling()
    }
}
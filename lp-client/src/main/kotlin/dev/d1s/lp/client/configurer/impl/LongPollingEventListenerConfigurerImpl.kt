package dev.d1s.lp.client.configurer.impl

import dev.d1s.lp.client.configurer.LongPollingEventListenerConfigurer
import dev.d1s.lp.client.listener.LongPollingEventListener
import dev.d1s.lp.client.poller.EventPoller
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry

internal class LongPollingEventListenerConfigurerImpl(
    private val longPollingEventListenerRegistry: LongPollingEventListenerRegistry,
    private val eventPoller: EventPoller
) : LongPollingEventListenerConfigurer {

    override suspend fun <T : Any> configureListener(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    ) {
        longPollingEventListenerRegistry[group, principal, type] = listener
        eventPoller.startPolling(group, principal, type)
    }
}
package dev.d1s.lp.client.registry

import dev.d1s.lp.client.listener.ListenerGroup
import dev.d1s.lp.client.listener.LongPollingEventListener

internal interface LongPollingEventListenerRegistry {

    operator fun <T : Any> set(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    )

    operator fun <T : Any> get(
        group: String,
        principal: String?,
        type: Class<T>
    ): ListenerGroup<T>?
}
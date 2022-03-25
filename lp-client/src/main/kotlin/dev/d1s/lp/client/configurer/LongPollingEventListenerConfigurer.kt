package dev.d1s.lp.client.configurer

import dev.d1s.lp.client.listener.LongPollingEventListener

internal interface LongPollingEventListenerConfigurer {

    suspend fun <T : Any> configureListener(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    )
}
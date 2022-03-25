package dev.d1s.lp.client.listener

import java.util.concurrent.CopyOnWriteArraySet

public data class ListenerGroup<T : Any>(
    val group: String,
    val principal: String?,
    val dataType: Class<out T>,
    val listeners: MutableSet<LongPollingEventListener<T>> = CopyOnWriteArraySet()
)
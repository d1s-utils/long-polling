package dev.d1s.lp.client.registry.impl

import dev.d1s.lp.client.listener.ListenerGroup
import dev.d1s.lp.client.listener.LongPollingEventListener
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry
import java.util.concurrent.CopyOnWriteArraySet

internal class LongPollingEventListenerRegistryImpl : LongPollingEventListenerRegistry {

    private val listenerGroups: MutableSet<ListenerGroup<*>> = CopyOnWriteArraySet()

    override operator fun <T : Any> set(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    ) {
        this[group, principal, type] ?: run {
            listenerGroups += ListenerGroup(group, principal, type).apply {
                listeners.add(listener)
            }
        }
    }

    override operator fun <T : Any> get(
        group: String,
        principal: String?,
        type: Class<T>
    ): ListenerGroup<T>? =
        @Suppress("UNCHECKED_CAST")
        listenerGroups.firstOrNull {
            it.group == group
                    && principal?.equals(it.principal) ?: true
                    && it.dataType.isAssignableFrom(type)
        }?.let {
            it as ListenerGroup<T>
        }
}
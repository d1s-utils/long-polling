/*
 * Copyright 2022 Mikhail Titov and other contributors (if even present)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.d1s.lp.client.registry.impl

import dev.d1s.lp.client.listener.ListenerGroup
import dev.d1s.lp.client.listener.LongPollingEventListener
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry
import java.util.concurrent.CopyOnWriteArraySet

internal class LongPollingEventListenerRegistryImpl : LongPollingEventListenerRegistry {

    private val listenerGroups: MutableSet<ListenerGroup<*>> = CopyOnWriteArraySet()

    override operator fun <T> set(
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

    override operator fun <T> get(
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
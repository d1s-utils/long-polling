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

package dev.d1s.lp.client.api

import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.factory.eventPoller
import dev.d1s.lp.client.factory.longPollingEventListenerConfigurer
import dev.d1s.lp.client.factory.longPollingEventListenerRegistry
import dev.d1s.lp.client.listener.LongPollingEventListener

public class LongPollingClient {

    private val longPollingEventListenerRegistry =
        longPollingEventListenerRegistry()
    private val eventPoller =
        eventPoller(longPollingEventListenerRegistry)
    private val longPollingEventListenerConfigurer =
        longPollingEventListenerConfigurer(longPollingEventListenerRegistry, eventPoller)

    public suspend fun <T : Any> onEvent(
        group: String,
        principal: String?,
        type: Class<T>,
        listener: LongPollingEventListener<T>
    ) {
        longPollingEventListenerConfigurer.configureListener(
            group,
            principal,
            type,
            listener
        )
    }

    public suspend inline fun <reified T : Any> onEvent(
        group: String,
        principal: String? = null,
        noinline listener: LongPollingEventListener<T>
    ) {
        this.onEvent(
            group,
            principal,
            T::class.java,
            listener
        )
    }

    public fun availableGroups(): Set<String> =
        eventPoller.availableGroups()

    public fun block() {
        eventPoller.block()
    }

    public fun updateConfiguration(
        configuration: EventPollerConfiguration.() -> Unit
    ) {
        eventPoller.updateConfiguration(configuration)
    }

    public fun stopPolling() {
        eventPoller.stopPolling()
    }
}
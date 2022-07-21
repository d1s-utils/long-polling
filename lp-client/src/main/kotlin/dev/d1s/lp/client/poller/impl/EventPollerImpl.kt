/*
 * Copyright 2022 Mikhail Titov
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

package dev.d1s.lp.client.poller.impl

import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.constant.RECIPIENT_NOT_SET_ERROR
import dev.d1s.lp.client.factory.defaultEventPollerConfiguration
import dev.d1s.lp.client.factory.longPollingEventService
import dev.d1s.lp.client.poller.EventPoller
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry
import dev.d1s.lp.client.strategy.DelayCalculationStrategy
import dev.d1s.lp.commons.entity.LongPollingEvent
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.properties.Delegates

internal class EventPollerImpl(
    private val longPollingEventListenerRegistry: LongPollingEventListenerRegistry
) : EventPoller {

    private val eventPollerConfiguration = AtomicReference(
        defaultEventPollerConfiguration()
    )

    private val longPollingEventService = longPollingEventService(
        eventPollerConfiguration.get()
    )

    private var active = true

    private val jobs = mutableSetOf<Job>()

    override suspend fun <T> startPolling(
        group: String,
        principal: String?,
        type: Class<T>
    ) {
        coroutineScope {
            jobs += launch {
                var events: Set<LongPollingEvent<T>> by Delegates.notNull()
                val delayCalculationStrategy =
                    eventPollerConfiguration.get().delayCalculationStrategy
                val recipient =
                    eventPollerConfiguration.get().recipient
                        ?: throw IllegalStateException(
                            RECIPIENT_NOT_SET_ERROR
                        )

                while (active) {
                    events = when (delayCalculationStrategy) {
                        is DelayCalculationStrategy.Fixed -> {
                            longPollingEventService.getEvents(
                                group,
                                principal,
                                recipient
                            )
                        }

                        is DelayCalculationStrategy.Adjusted -> {
                            delayCalculationStrategy.measure {
                                longPollingEventService.getEvents(
                                    group,
                                    principal,
                                    recipient
                                )
                            }
                        }
                    }

                    events.forEach { event ->
                        longPollingEventListenerRegistry[group, principal, type]
                            ?.listeners?.forEach { listener ->
                                launch {
                                    listener(event)
                                }
                            }
                    }

                    delay(delayCalculationStrategy.delay.toMillis())
                }
            }
        }
    }

    override fun block() {
        runBlocking {
            jobs.forEach {
                it.join()
            }
        }
    }

    override fun updateConfiguration(configuration: EventPollerConfiguration.() -> Unit) {
        eventPollerConfiguration.set(eventPollerConfiguration.get().apply(configuration))
    }

    override fun stopPolling() {
        active = false
    }

    override fun availableGroups(): Set<String> =
        longPollingEventService.getAvailableGroups()
}
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

package dev.d1s.lp.client.factory

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.configurer.LongPollingEventListenerConfigurer
import dev.d1s.lp.client.configurer.impl.LongPollingEventListenerConfigurerImpl
import dev.d1s.lp.client.poller.EventPoller
import dev.d1s.lp.client.poller.impl.EventPollerImpl
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry
import dev.d1s.lp.client.registry.impl.LongPollingEventListenerRegistryImpl
import dev.d1s.lp.client.service.LongPollingEventService
import dev.d1s.lp.client.service.impl.LongPollingEventServiceImpl

internal fun longPollingEventListenerConfigurer(
    longPollingEventListenerRegistry: LongPollingEventListenerRegistry,
    eventPoller: EventPoller
): LongPollingEventListenerConfigurer =
    LongPollingEventListenerConfigurerImpl(longPollingEventListenerRegistry, eventPoller)

internal fun eventPoller(longPollingEventListenerRegistry: LongPollingEventListenerRegistry): EventPoller =
    EventPollerImpl(longPollingEventListenerRegistry)

internal fun longPollingEventListenerRegistry(): LongPollingEventListenerRegistry =
    LongPollingEventListenerRegistryImpl()

internal fun longPollingEventService(configuration: EventPollerConfiguration): LongPollingEventService =
    LongPollingEventServiceImpl(configuration)

internal fun defaultEventPollerConfiguration() = EventPollerConfiguration()

internal fun objectMapper() = ObjectMapper()
    .registerKotlinModule()
    .registerModule(JavaTimeModule())
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

package dev.d1s.lp.client.testUtil

import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.configurer.LongPollingEventListenerConfigurer
import dev.d1s.lp.client.factory.defaultEventPollerConfiguration
import dev.d1s.lp.client.listener.LongPollingEventListener
import dev.d1s.lp.client.poller.EventPoller
import dev.d1s.lp.client.registry.LongPollingEventListenerRegistry
import dev.d1s.lp.client.service.LongPollingEventService
import io.mockk.mockk
import io.mockk.spyk

internal val mockLongPollingEventListenerRegistry
    get() = mockk<LongPollingEventListenerRegistry>(relaxUnitFun = true)

internal val mockEventPoller
    get() = mockk<EventPoller>(relaxUnitFun = true)

internal val mockLongPollingEventListenerConfigurer
    get() = mockk<LongPollingEventListenerConfigurer>(relaxUnitFun = true)

internal val mockLongPollingEventService
    get() = mockk<LongPollingEventService>(relaxUnitFun = true)

internal val mockLongPollingEventListener: LongPollingEventListener<*> = {}

internal val mockEventPollerConfiguration: EventPollerConfiguration.() -> Unit = {}

internal val spyEventPollerConfiguration = spyk(defaultEventPollerConfiguration())
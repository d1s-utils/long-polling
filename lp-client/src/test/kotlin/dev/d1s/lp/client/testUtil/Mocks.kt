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
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
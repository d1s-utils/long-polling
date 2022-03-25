package dev.d1s.lp.client.service

import dev.d1s.lp.commons.domain.LongPollingEvent

internal interface LongPollingEventService {

    fun <T : Any> getEvents(
        group: String,
        principal: String?
    ): Set<LongPollingEvent<T>>
}
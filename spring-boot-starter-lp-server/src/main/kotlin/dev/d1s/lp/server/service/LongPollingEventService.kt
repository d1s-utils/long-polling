package dev.d1s.lp.server.service

import dev.d1s.lp.commons.domain.LongPollingEvent

public interface LongPollingEventService {

    public fun add(longPollingEvent: LongPollingEvent<*>): Set<LongPollingEvent<*>>

    public fun getByGroup(group: String): Set<LongPollingEvent<*>>

    public fun getByPrincipal(group: String, principal: String): Set<LongPollingEvent<*>>
}
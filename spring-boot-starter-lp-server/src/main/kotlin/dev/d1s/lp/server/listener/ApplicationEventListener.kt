package dev.d1s.lp.server.listener

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener

internal class ApplicationEventListener {

    @Autowired
    private lateinit var longPollingEventService: LongPollingEventService

    @EventListener
    fun interceptEvent(longPollingEvent: LongPollingEvent<*>) {
        longPollingEventService.add(longPollingEvent)
    }
}
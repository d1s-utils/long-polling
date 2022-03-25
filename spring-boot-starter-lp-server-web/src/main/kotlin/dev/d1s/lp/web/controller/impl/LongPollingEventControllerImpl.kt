package dev.d1s.lp.web.controller.impl

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.lp.web.controller.LongPollingEventController
import dev.d1s.security.configuration.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
internal class LongPollingEventControllerImpl : LongPollingEventController {

    @Autowired
    private lateinit var longPollingEventService: LongPollingEventService

    @Secured
    override fun getByGroup(group: String): Set<LongPollingEvent<*>> =
        longPollingEventService.getByGroup(group)

    @Secured
    override fun getByPrincipal(group: String, principal: String): Set<LongPollingEvent<*>> =
        longPollingEventService.getByPrincipal(group, principal)
}
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

package dev.d1s.lp.web.controller.impl

import dev.d1s.lp.commons.entity.LongPollingEvent
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
    override fun getByGroup(
        group: String,
        recipient: String
    ): Set<LongPollingEvent<*>> =
        longPollingEventService.getByGroup(group, recipient)

    @Secured
    override fun getByPrincipal(
        group: String,
        principal: String,
        recipient: String
    ): Set<LongPollingEvent<*>> =
        longPollingEventService.getByPrincipal(group, principal, recipient)

    @Secured
    override fun getEventGroups(): Set<String> =
        longPollingEventService.getAvailableGroups()
}
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

package dev.d1s.lp.server.listener

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.teabag.log4j.logger
import dev.d1s.teabag.log4j.util.lazyDebug
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener

internal class ApplicationEventListener {

    @Autowired
    private lateinit var longPollingEventService: LongPollingEventService

    private val log = logger()

    @EventListener
    fun interceptEvent(longPollingEvent: LongPollingEvent<*>) {
        log.lazyDebug {
            "Handled an event from the Spring event bus: $longPollingEvent"
        }

        longPollingEventService.add(longPollingEvent)
    }
}
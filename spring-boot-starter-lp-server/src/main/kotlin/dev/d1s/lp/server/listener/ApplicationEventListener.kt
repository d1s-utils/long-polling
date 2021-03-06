/*
 * Copyright 2022 Mikhail Titov
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

import dev.d1s.lp.commons.entity.LongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import org.lighthousegames.logging.logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener

internal class ApplicationEventListener {

    @set:Autowired
    lateinit var longPollingEventService: LongPollingEventService

    private val log = logging()

    @EventListener
    fun interceptEvent(longPollingEvent: LongPollingEvent<*>) {
        log.d {
            "Handled event from Spring: $longPollingEvent"
        }

        longPollingEventService.add(longPollingEvent)
    }
}
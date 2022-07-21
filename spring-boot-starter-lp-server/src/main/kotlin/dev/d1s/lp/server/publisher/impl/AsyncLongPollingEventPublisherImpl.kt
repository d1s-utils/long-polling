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

package dev.d1s.lp.server.publisher.impl

import dev.d1s.lp.commons.entity.LongPollingEvent
import dev.d1s.lp.server.publisher.AsyncLongPollingEventPublisher
import dev.d1s.lp.server.service.LongPollingEventService
import org.lighthousegames.logging.logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.CompletableFuture

@Component
internal class AsyncLongPollingEventPublisherImpl : AsyncLongPollingEventPublisher {

    @set:Autowired
    lateinit var longPollingEventService: LongPollingEventService

    private val log = logging()

    @Async
    override fun <T> publish(
        group: String,
        principal: String?,
        data: T?
    ): CompletableFuture<LongPollingEvent<T>> {
        val event = LongPollingEvent(
            group,
            principal,
            data,
            mutableSetOf(),
            Instant.now()
        )

        log.d {
            "Publishing event asynchronously: $event"
        }

        longPollingEventService.add(event)

        return CompletableFuture.completedFuture(event)
    }
}
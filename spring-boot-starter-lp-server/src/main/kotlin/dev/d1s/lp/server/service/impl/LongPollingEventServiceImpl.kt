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

package dev.d1s.lp.server.service.impl

import dev.d1s.lp.commons.entity.LongPollingEvent
import dev.d1s.lp.server.configurer.LongPollingServerConfigurer
import dev.d1s.lp.server.exception.IncompatibleEventDataTypeException
import dev.d1s.lp.server.exception.UnavailableEventGroupException
import dev.d1s.lp.server.properties.LongPollingEventServerConfigurationProperties
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.teabag.log4j.logger
import dev.d1s.teabag.log4j.util.lazyDebug
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

@Service
internal class LongPollingEventServiceImpl : LongPollingEventService, InitializingBean {

    @Autowired
    private lateinit var properties: LongPollingEventServerConfigurationProperties

    @Autowired
    private lateinit var longPollingServerConfigurer: LongPollingServerConfigurer

    @Autowired
    private lateinit var scheduler: ThreadPoolTaskScheduler

    private val events = CopyOnWriteArrayList<LongPollingEvent<*>>()

    private lateinit var availableGroups: Set<String>

    private val log = logger()

    override fun add(longPollingEvent: LongPollingEvent<*>): Set<LongPollingEvent<*>> {
        log.lazyDebug {
            "adding event $longPollingEvent"
        }

        longPollingEvent.group.checkGroup()

        events.forEach {
            if (it.data::class != longPollingEvent.data::class) {
                throw IncompatibleEventDataTypeException
            }
        }

        longPollingEvent.id = UUID.randomUUID().toString()

        events += longPollingEvent

        log.lazyDebug {
            "added event $longPollingEvent, scheduling for deletion. " +
                    "(event lifetime: ${properties.eventLifetime})"
        }

        scheduler.schedule(
            {
                log.lazyDebug {
                    "deleting event $longPollingEvent"
                }

                events.remove(longPollingEvent)
            },
            Instant.now() + properties.eventLifetime
        )

        return events.toSet()
    }

    override fun getByGroup(group: String, recipient: String): Set<LongPollingEvent<*>> {
        group.checkGroup()

        return events.filter {
            it.group == group && !it.satisfiedRecipients.contains(recipient)
        }.toSet().also {
            it.setSatisfiedRecipient(recipient)

            log.lazyDebug {
                "returning events for group $group and recipient $recipient: $it"
            }
        }
    }

    override fun getByPrincipal(
        group: String,
        principal: String,
        recipient: String
    ): Set<LongPollingEvent<*>> {
        group.checkGroup()

        return events.filter {
            it.group == group
                    && it.principal == principal
                    && !it.satisfiedRecipients.contains(recipient)
        }.toSet().also {
            it.setSatisfiedRecipient(recipient)

            log.lazyDebug {
                "returning events for group $group, principal $principal and recipient $recipient: $it"
            }
        }
    }

    override fun getAvailableGroups(): Set<String> = availableGroups

    override fun afterPropertiesSet() {
        availableGroups = longPollingServerConfigurer.getAvailableGroups()

        log.lazyDebug {
            "initialized available long polling groups: $availableGroups"
        }
    }

    private fun Set<LongPollingEvent<*>>.setSatisfiedRecipient(recipient: String) {
        this.forEach {
            it.satisfiedRecipients += recipient
        }
    }

    private fun String.checkGroup() {
        if (!availableGroups.contains(this)) {
            throw UnavailableEventGroupException
        }
    }
}
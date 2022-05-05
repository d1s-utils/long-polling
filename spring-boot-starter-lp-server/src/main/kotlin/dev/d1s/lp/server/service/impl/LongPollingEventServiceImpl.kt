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

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.server.constant.LONG_POLLING_EVENT_CACHE
import dev.d1s.lp.server.exception.EventGroupNotFoundException
import dev.d1s.lp.server.exception.IncompatibleEventDataTypeException
import dev.d1s.lp.server.properties.LongPollingEventServerConfigurationProperties
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.teabag.log4j.logger
import dev.d1s.teabag.log4j.util.lazyDebug
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

@Service
internal class LongPollingEventServiceImpl : LongPollingEventService {

    @Autowired
    private lateinit var cacheManager: CacheManager

    @Autowired
    private lateinit var properties: LongPollingEventServerConfigurationProperties

    private val eventCache by lazy {
        cacheManager.getCache(LONG_POLLING_EVENT_CACHE)
    }

    private val executor = Executors.newScheduledThreadPool(3)

    private val log = logger()

    override fun add(longPollingEvent: LongPollingEvent<*>): Set<LongPollingEvent<*>> {
        return this.getSet(longPollingEvent.group, createGroup = true, copySet = false) {
            forEach {
                if (it.data::class != longPollingEvent.data::class) {
                    throw IncompatibleEventDataTypeException()
                }
            }

            add(longPollingEvent)

            executor.schedule({
                remove(longPollingEvent)
            }, properties.eventLifeTime.toMillis(), TimeUnit.MILLISECONDS)

            log.lazyDebug {
                "Event $longPollingEvent is now available from the long-polling API."
            }
        }
    }

    override fun getByGroup(group: String): Set<LongPollingEvent<*>> =
        this.getSet(group) {
            removeAll(this)
        }.also {
            log.lazyDebug {
                "Got all events associated by the provided group ($group): $it"
            }
        }

    override fun getByPrincipal(group: String, principal: String): Set<LongPollingEvent<*>> {
        var filteredSet: Set<LongPollingEvent<*>> by Delegates.notNull()

        this.getSet(group) {
            filteredSet = filter {
                it.principal == principal
            }.toSet()

            removeAll(filteredSet)
        }

        log.lazyDebug {
            "Got all events associated by the provided group and principal ($group, $principal): $filteredSet"
        }

        return filteredSet
    }

    private fun getSet(
        eventGroup: String,
        createGroup: Boolean = false,
        copySet: Boolean = true,
        cacheOperation: MutableSet<LongPollingEvent<*>>.() -> Unit
    ): Set<LongPollingEvent<*>> =
        eventCache[eventGroup]?.let {
            @Suppress("UNCHECKED_CAST")
            this.alteredSet(copySet, cacheOperation, eventGroup, it.get()!! as MutableSet<LongPollingEvent<*>>)
        } ?: run {
            if (createGroup) {
                this.alteredSet(copySet, cacheOperation, eventGroup, CopyOnWriteArraySet())
            } else {
                throw EventGroupNotFoundException()
            }
        }

    private fun alteredSet(
        copySet: Boolean,
        cacheOperation: MutableSet<LongPollingEvent<*>>.() -> Unit,
        eventGroup: String,
        originSet: MutableSet<LongPollingEvent<*>>
    ): Set<LongPollingEvent<*>> {
        var copiedSet: Set<LongPollingEvent<*>> by Delegates.notNull()

        if (copySet) {
            copiedSet = originSet.toSet()
        }

        cacheOperation(originSet)
        eventCache.put(eventGroup, originSet)

        return if (copySet) {
            copiedSet
        } else {
            originSet
        }
    }
}
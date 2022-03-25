package dev.d1s.lp.server.service.impl

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.server.constant.LONG_POLLING_EVENT_CACHE
import dev.d1s.lp.server.exception.EventGroupNotFoundException
import dev.d1s.lp.server.exception.IncompatibleEventDataTypeException
import dev.d1s.lp.server.properties.LongPollingEventServerConfigurationProperties
import dev.d1s.lp.server.service.LongPollingEventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.concurrent.CopyOnWriteArraySet
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

    override fun add(longPollingEvent: LongPollingEvent<*>): Set<LongPollingEvent<*>> {
        return this.getSet(longPollingEvent.group, createGroup = true, copySet = false) {
            forEach {
                if (it.data::class != longPollingEvent.data::class) {
                    throw IncompatibleEventDataTypeException()
                }
            }

            add(longPollingEvent)
        }
    }

    override fun getByGroup(group: String): Set<LongPollingEvent<*>> =
        this.getSet(group) {
            removeAll(this)
        }

    override fun getByPrincipal(group: String, principal: String): Set<LongPollingEvent<*>> {
        var filteredSet: Set<LongPollingEvent<*>> by Delegates.notNull()

        this.getSet(group) {
            filteredSet = filter {
                it.principal == principal
            }.toSet()

            removeAll(filteredSet)
        }

        return filteredSet
    }

    private fun getSet(
        eventGroup: String,
        createGroup: Boolean = false,
        copySet: Boolean = true,
        cacheOperation: MutableSet<LongPollingEvent<*>>.() -> Unit
    ): Set<LongPollingEvent<*>> {
        eventCache[eventGroup]?.let {
            @Suppress("UNCHECKED_CAST")
            val set = it.get()!! as MutableSet<LongPollingEvent<*>>

            set.forEach { event ->
                if (event.timestamp + properties.eventLifeTime < Instant.now()) {
                    set.remove(event)
                }
            }

            return this.alteredSet(copySet, cacheOperation, eventGroup, set)
        } ?: run {
            if (createGroup) {
                return this.alteredSet(copySet, cacheOperation, eventGroup, CopyOnWriteArraySet())
            } else {
                throw EventGroupNotFoundException()
            }
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
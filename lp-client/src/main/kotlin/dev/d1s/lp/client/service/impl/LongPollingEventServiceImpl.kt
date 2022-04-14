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

package dev.d1s.lp.client.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.exception.PollFailedException
import dev.d1s.lp.client.factory.objectMapper
import dev.d1s.lp.client.service.LongPollingEventService
import dev.d1s.lp.client.strategy.FallbackStrategy
import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.teabag.stdlib.text.replacePlaceholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.net.URI

internal class LongPollingEventServiceImpl(
    private val configuration: EventPollerConfiguration
) : LongPollingEventService {

    private val mapper = objectMapper()

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun <T : Any> getEvents(
        group: String,
        principal: String?
    ): Set<LongPollingEvent<T>> =
        principal?.let {
            this.getByPrincipal(group, principal)
        } ?: this.getByGroup(group)

    private fun <T : Any> getByGroup(group: String): Set<LongPollingEvent<T>> =
        this.getEvents(
            resolvePath(
                configuration.getEventsByGroupPath
                    .setGroup(group)
            )
        )

    private fun <T : Any> getByPrincipal(group: String, principal: String): Set<LongPollingEvent<T>> =
        this.getEvents(
            resolvePath(
                configuration.getEventsByPrincipalPath
                    .setGroup(group)
                    .setPrincipal(principal)
            )
        )

    private fun String.setGroup(group: String) = this.replacePlaceholder("group" to group)
    private fun String.setPrincipal(principal: String) = this.replacePlaceholder("principal" to principal)

    private fun resolvePath(path: String): URI =
        URI(
            configuration.host
                ?: throw IllegalStateException("Server host is not present in the configuration.")
        ).resolve(path)

    private tailrec fun <T : Any> getEvents(uri: URI): Set<LongPollingEvent<T>> {
        val (_, _, result) = Fuel.get(uri.toString()).apply {
            configuration.authorization?.let {
                header("Authorization" to it)
            }
        }.response()

        // literally golang
        val (bytes, error) = result

        error?.let {
            when (val fallbackStrategy = configuration.fallbackStrategy) {
                is FallbackStrategy.Fail -> {
                    this.fail(uri, it)
                }

                is FallbackStrategy.Retry -> {
                    if (it.response.statusCode >= 400) {
                        // there's no need to bombard the server with retries in this case
                        this.fail(uri, it)
                    }

                    val delay = fallbackStrategy.retryDelay
                    logger.warn("Poll failed. Retrying in $delay")

                    runBlocking {
                        delay(delay.toMillis())
                    }

                    return this.getEvents(uri)
                }
            }
        }

        return mapper.readValue(
            bytes!!,
            object : TypeReference<Set<LongPollingEvent<T>>>() {}
        )
    }

    private fun fail(uri: URI, fuelError: FuelError) {
        throw PollFailedException("Poll to $uri has been failed: $fuelError")
    }
}
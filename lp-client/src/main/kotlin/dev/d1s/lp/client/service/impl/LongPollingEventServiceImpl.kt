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
import com.github.kittinunf.fuel.core.Request
import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.constant.POLL_FAILED_ERROR
import dev.d1s.lp.client.constant.SERVER_BASE_URL_NOT_CONFIGURED_ERROR
import dev.d1s.lp.client.exception.PollFailedException
import dev.d1s.lp.client.factory.objectMapper
import dev.d1s.lp.client.service.LongPollingEventService
import dev.d1s.lp.client.strategy.FallbackStrategy
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENT_GROUPS_MAPPING
import dev.d1s.lp.commons.entity.LongPollingEvent
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
        principal: String?,
        recipient: String
    ): Set<LongPollingEvent<T>> =
        principal?.let {
            this.getByPrincipal(group, principal, recipient)
        } ?: this.getByGroup(group, recipient)

    override fun getAvailableGroups(): Set<String> {
        val uri = this.resolvePath(
            GET_EVENT_GROUPS_MAPPING
        )

        val (_, _, result) = Fuel.get(
            uri.toString()
        ).apply {
            setAuthorization()
        }.response()

        val (bytes, error) = result

        error?.let {
            fail(uri, error)
        }

        return mapper.readValue(
            bytes!!,
            object : TypeReference<Set<String>>() {}
        )
    }

    private fun <T : Any> getByGroup(
        group: String,
        recipient: String
    ): Set<LongPollingEvent<T>> =
        this.getEvents(
            this.resolvePath(
                GET_EVENTS_BY_GROUP_MAPPING
                    .setGroup(group)
            ),
            recipient
        )

    private fun <T : Any> getByPrincipal(
        group: String,
        principal: String,
        recipient: String
    ): Set<LongPollingEvent<T>> =
        this.getEvents(
            this.resolvePath(
                GET_EVENTS_BY_PRINCIPAL_MAPPING
                    .setGroup(group)
                    .setPrincipal(principal)
            ),
            recipient
        )

    private fun String.setGroup(group: String) =
        this.replacePlaceholder("group" to group)

    private fun String.setPrincipal(principal: String) =
        this.replacePlaceholder("principal" to principal)

    private fun resolvePath(path: String): URI =
        URI(
            configuration.baseUrl
                ?: throw IllegalStateException(
                    SERVER_BASE_URL_NOT_CONFIGURED_ERROR
                )
        ).resolve(path)

    private fun <T : Any> getEvents(uri: URI, recipient: String): Set<LongPollingEvent<T>> {
        val (_, _, result) = Fuel.get(
            uri.toString(),
            listOf(
                RECIPIENT_PARAMETER to recipient
            )
        ).apply {
            setAuthorization()
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

                    return this.getEvents(uri, recipient)
                }
            }
        }

        return mapper.readValue(
            bytes!!,
            object : TypeReference<Set<LongPollingEvent<T>>>() {}
        )
    }

    private fun fail(uri: URI, fuelError: FuelError) {
        throw PollFailedException(
            POLL_FAILED_ERROR.format(uri, fuelError)
        )
    }

    private fun Request.setAuthorization() {
        configuration.authorization?.let {
            header(AUTHORIZATION_HEADER to it)
        }
    }

    private companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"

        private const val RECIPIENT_PARAMETER = "recipient"
    }
}
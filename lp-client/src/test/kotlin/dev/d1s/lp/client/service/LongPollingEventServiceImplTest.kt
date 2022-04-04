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

package dev.d1s.lp.client.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.ResponseResultOf
import dev.d1s.lp.client.configuration.EventPollerConfiguration
import dev.d1s.lp.client.exception.PollFailedException
import dev.d1s.lp.client.factory.objectMapper
import dev.d1s.lp.client.service.impl.LongPollingEventServiceImpl
import dev.d1s.lp.client.strategy.FallbackStrategy
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import strikt.api.expectThat
import strikt.assertions.containsExactly
import java.time.Duration

internal class LongPollingEventServiceImplTest {

    private val objectMapper = mockk<ObjectMapper>()

    private val mockResponse = mockk<ResponseResultOf<ByteArray>>()

    private val mockResult = mockk<com.github.kittinunf.result.Result<ByteArray, FuelError>>()

    private val mockError = mockk<FuelError>(relaxed = true)

    private val realObjectMapper = objectMapper()

    private val mockConfiguration = mockk<EventPollerConfiguration>()

    private val event = mockLongPollingEvent

    private val eventSet = setOf(event)

    private val eventBytes = realObjectMapper.writeValueAsBytes(event)

    private val getEventsByGroupConcatenated = "https://d1s.dev/events/$VALID_STUB"
    private val getEventsByPrincipalConcatenated = "https://d1s.dev/events/$VALID_STUB/$VALID_STUB"

    @BeforeEach
    fun setup() {
        every {
            mockResponse.component3() // result
        } returns mockResult

        every {
            mockConfiguration.host
        } returns "https://d1s.dev"

        every {
            mockConfiguration.getEventsByGroupPath
        } returns GET_EVENTS_BY_GROUP_MAPPING

        every {
            mockConfiguration.getEventsByPrincipalPath
        } returns GET_EVENTS_BY_PRINCIPAL_MAPPING

        every {
            mockConfiguration.authorization
        } returns null

        every {
            mockResult.component1() // bytes
        } returns realObjectMapper.writeValueAsBytes(event)

        every {
            mockResult.component2() // error
        } returns null

        every {
            mockConfiguration.fallbackStrategy
        } returns FallbackStrategy.Fail

        every {
            objectMapper.readValue(
                eventBytes,
                any<TypeReference<Set<LongPollingEvent<String>>>>()
            )
        } returns eventSet
    }

    @Test
    fun `should get event by group`() {
        this.tryGetEvents(false)
    }

    @Test
    fun `should get events by principal`() {
        this.tryGetEvents(true)
    }

    @Test
    fun `should throw PollFailedException if the FallbackStrategy is Fail`() {
        every {
            mockResult.component2() // error
        } returns mockError

        this.newInstanceWithMockedDeps {
            assertThrows<PollFailedException> {
                it.getEvents<String>(VALID_STUB, VALID_STUB)
            }
        }
    }

    @Test
    fun `should throw PollFailedException if the FallbackStrategy is Retry and status code is greater or equal to 400`() {
        every {
            mockResult.component2() // error
        } returns mockError

        every {
            mockConfiguration.fallbackStrategy
        } returns FallbackStrategy.Retry(Duration.ZERO)

        every {
            mockError.response.statusCode
        } returns 400

        this.newInstanceWithMockedDeps {
            assertThrows<PollFailedException> {
                it.getEvents<String>(VALID_STUB, VALID_STUB)
            }
        }
    }

    private inline fun newInstanceWithMockedDeps(block: (LongPollingEventServiceImpl) -> Unit) {
        mockkStatic("dev.d1s.lp.client.factory.InternalComponentsKt") {
            every {
                objectMapper()
            } returns objectMapper

            mockkObject(Fuel) {
                every {
                    Fuel.get(any<String>()).response()
                } returns mockResponse

                block(LongPollingEventServiceImpl(mockConfiguration))
            }
        }
    }

    private fun tryGetEvents(byPrincipal: Boolean) {
        this.newInstanceWithMockedDeps {
            val events = assertDoesNotThrow {
                it.getEvents<String>(
                    VALID_STUB, if (byPrincipal) {
                        VALID_STUB
                    } else {
                        null
                    }
                )
            }

            expectThat(events).containsExactly(event)

            verify {
                if (byPrincipal) {
                    mockConfiguration.getEventsByPrincipalPath
                } else {
                    mockConfiguration.getEventsByGroupPath
                }
            }

            verify {
                Fuel.get(
                    if (byPrincipal) {
                        getEventsByPrincipalConcatenated
                    } else {
                        getEventsByGroupConcatenated
                    }
                )
            }

            verify {
                mockConfiguration.authorization
            }

            verify {
                mockResult.component1()
            }

            verify {
                mockResult.component2()
            }

            verify {
                objectMapper.readValue(
                    eventBytes,
                    any<TypeReference<Set<LongPollingEvent<String>>>>()
                )
            }
        }
    }
}
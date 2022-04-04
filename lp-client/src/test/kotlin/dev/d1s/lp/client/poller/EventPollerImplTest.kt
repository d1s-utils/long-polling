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

package dev.d1s.lp.client.poller

import dev.d1s.lp.client.factory.defaultEventPollerConfiguration
import dev.d1s.lp.client.factory.longPollingEventService
import dev.d1s.lp.client.poller.impl.EventPollerImpl
import dev.d1s.lp.client.testUtil.mockLongPollingEventListenerRegistry
import dev.d1s.lp.client.testUtil.mockLongPollingEventService
import dev.d1s.lp.client.testUtil.spyEventPollerConfiguration
import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class EventPollerImplTest {

    private val longPollingEventListenerRegistry =
        mockLongPollingEventListenerRegistry

    private val eventPollerConfiguration =
        spyEventPollerConfiguration

    private val longPollingEventService =
        mockLongPollingEventService

    private val event = mockLongPollingEvent

    @BeforeEach
    fun setup() {
        every {
            longPollingEventService.getEvents<String>(VALID_STUB, VALID_STUB)
        } returns setOf(event)

        every {
            longPollingEventListenerRegistry[VALID_STUB, VALID_STUB, String::class.java]
        } returns null
    }

    @Test
    fun `should start polling and then stop`() {
        this.newInstanceWithMockedDeps {
            runBlocking {
                launch {
                    assertDoesNotThrow {
                        it.startPolling(VALID_STUB, VALID_STUB, String::class.java)
                    }
                }

                delay(100)

                assertDoesNotThrow {
                    it.stopPolling()
                }
            }

            verify {
                eventPollerConfiguration.delayCalculationStrategy
            }

            verify {
                longPollingEventService.getEvents<String>(VALID_STUB, VALID_STUB)
            }

            verify {
                longPollingEventListenerRegistry[VALID_STUB, VALID_STUB, String::class.java]
            }
        }
    }

    private inline fun newInstanceWithMockedDeps(block: (EventPollerImpl) -> Unit) {
        mockkStatic("dev.d1s.lp.client.factory.InternalComponentsKt") {
            every {
                defaultEventPollerConfiguration()
            } returns eventPollerConfiguration

            every {
                longPollingEventService(eventPollerConfiguration)
            } returns longPollingEventService

            block(EventPollerImpl(longPollingEventListenerRegistry))
        }
    }
}
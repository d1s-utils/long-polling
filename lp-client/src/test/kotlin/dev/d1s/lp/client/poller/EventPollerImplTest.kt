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
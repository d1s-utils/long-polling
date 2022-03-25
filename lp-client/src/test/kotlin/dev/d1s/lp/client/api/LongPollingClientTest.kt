package dev.d1s.lp.client.api

import dev.d1s.lp.client.factory.eventPoller
import dev.d1s.lp.client.factory.longPollingEventListenerConfigurer
import dev.d1s.lp.client.factory.longPollingEventListenerRegistry
import dev.d1s.lp.client.testUtil.*
import dev.d1s.teabag.testing.assertDoesNotThrowBlocking
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class LongPollingClientTest {

    private val longPollingEventListenerRegistry =
        mockLongPollingEventListenerRegistry

    private val longPollingEventListenerConfigurer =
        mockLongPollingEventListenerConfigurer

    private val eventPoller = mockEventPoller

    @Test
    fun `should configure the event listener`() {
        val listener = mockLongPollingEventListener

        this.newInstanceWithMockedDeps {
            assertDoesNotThrowBlocking {
                it.onEvent(VALID_STUB, VALID_STUB, Any::class.java, listener)
            }

            coVerify {
                longPollingEventListenerConfigurer.configureListener(
                    VALID_STUB,
                    VALID_STUB,
                    Any::class.java,
                    listener
                )
            }
        }
    }

    @Test
    fun `should block the thread`() {
        this.newInstanceWithMockedDeps {
            assertDoesNotThrow {
                it.block()
            }

            verify {
                eventPoller.block()
            }
        }
    }

    @Test
    fun `should update the configuration`() {
        val configuration = mockEventPollerConfiguration

        this.newInstanceWithMockedDeps {
            assertDoesNotThrow {
                it.updateConfiguration(configuration)
            }

            verify {
                eventPoller.updateConfiguration(configuration)
            }
        }
    }

    @Test
    fun `should stop polling`() {
        this.newInstanceWithMockedDeps {
            assertDoesNotThrow {
                it.stopPolling()
            }

            verify {
                eventPoller.stopPolling()
            }
        }
    }

    private inline fun newInstanceWithMockedDeps(block: (LongPollingClient) -> Unit) {
        mockkStatic("dev.d1s.lp.client.factory.InternalComponentsKt") {
            every {
                longPollingEventListenerRegistry()
            } returns longPollingEventListenerRegistry

            every {
                eventPoller(any())
            } returns eventPoller

            every {
                longPollingEventListenerConfigurer(any(), any())
            } returns longPollingEventListenerConfigurer

            block(LongPollingClient())
        }
    }
}
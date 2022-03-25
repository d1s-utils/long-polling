package dev.d1s.lp.client.configurer

import dev.d1s.lp.client.configurer.impl.LongPollingEventListenerConfigurerImpl
import dev.d1s.lp.client.testUtil.mockEventPoller
import dev.d1s.lp.client.testUtil.mockLongPollingEventListener
import dev.d1s.lp.client.testUtil.mockLongPollingEventListenerRegistry
import dev.d1s.teabag.testing.assertDoesNotThrowBlocking
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.coVerify
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class LongPollingEventListenerConfigurerImplTest {

    private val longPollingEventListenerRegistry =
        mockLongPollingEventListenerRegistry

    private val eventPoller =
        mockEventPoller

    private val configurer = LongPollingEventListenerConfigurerImpl(
        longPollingEventListenerRegistry,
        eventPoller
    )

    @Test
    fun `should configure the listener`() {
        val listener = mockLongPollingEventListener

        assertDoesNotThrowBlocking {
            configurer.configureListener(VALID_STUB, VALID_STUB, Any::class.java, listener)
        }

        verify {
            longPollingEventListenerRegistry[VALID_STUB, VALID_STUB, Any::class.java] = listener
        }

        coVerify {
            eventPoller.startPolling(VALID_STUB, VALID_STUB, Any::class.java)
        }
    }
}
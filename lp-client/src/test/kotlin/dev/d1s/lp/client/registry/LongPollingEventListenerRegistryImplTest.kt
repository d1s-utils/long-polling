package dev.d1s.lp.client.registry

import dev.d1s.lp.client.listener.ListenerGroup
import dev.d1s.lp.client.registry.impl.LongPollingEventListenerRegistryImpl
import dev.d1s.lp.client.testUtil.mockLongPollingEventListener
import dev.d1s.teabag.testing.constant.VALID_STUB
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class LongPollingEventListenerRegistryImplTest {

    @Test
    fun `should add and get the event listener`() {
        val registry = LongPollingEventListenerRegistryImpl()
        val listener = mockLongPollingEventListener

        assertDoesNotThrow {
            registry[VALID_STUB, VALID_STUB, Any::class.java] = listener
        }

        val returnedListener = assertDoesNotThrow {
            registry[VALID_STUB, VALID_STUB, Any::class.java]
        }

        expectThat(returnedListener) isEqualTo ListenerGroup(
            VALID_STUB,
            VALID_STUB,
            Any::class.java,
            mutableSetOf(listener)
        )
    }
}
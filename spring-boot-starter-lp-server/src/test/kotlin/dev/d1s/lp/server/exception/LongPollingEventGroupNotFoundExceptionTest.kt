package dev.d1s.lp.server.exception

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class LongPollingEventGroupNotFoundExceptionTest {

    @Test
    fun `should return valid exception message`() {
        expectThat(EventGroupNotFoundException().message) isEqualTo
                "LongPollingEvent group was not found."
    }
}
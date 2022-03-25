package dev.d1s.lp.server.exception

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class IncompatibleEventDataTypeExceptionTest {

    @Test
    fun `should return valid exception message`() {
        expectThat(IncompatibleEventDataTypeException().message) isEqualTo
                "Event group must contain events with the same type of data."
    }
}
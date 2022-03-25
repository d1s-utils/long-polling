package dev.d1s.lp.commons.test

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.teabag.testing.constant.VALID_STUB
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.Instant

internal class MockLongPollingEventTest {

    @Test
    fun `should return valid event`() {
        expectThat(mockLongPollingEvent) isEqualTo LongPollingEvent(
            VALID_STUB,
            Instant.EPOCH,
            VALID_STUB,
            VALID_STUB
        )
    }
}
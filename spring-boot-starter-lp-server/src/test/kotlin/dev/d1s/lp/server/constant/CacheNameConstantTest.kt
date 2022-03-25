package dev.d1s.lp.server.constant

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class CacheNameConstantTest {

    @Test
    fun `should return valid cache name`() {
        expectThat(LONG_POLLING_EVENT_CACHE) isEqualTo "long-polling-event"
    }
}
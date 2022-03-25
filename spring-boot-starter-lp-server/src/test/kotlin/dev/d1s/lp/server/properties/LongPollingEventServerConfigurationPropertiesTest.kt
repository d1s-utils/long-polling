package dev.d1s.lp.server.properties

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.time.Duration

internal class LongPollingEventServerConfigurationPropertiesTest {

    @Test
    fun `should return valid default eventLifeTime value`() {
        expectThat(LongPollingEventServerConfigurationProperties().eventLifeTime) isEqualTo
                Duration.ofSeconds(5)
    }
}
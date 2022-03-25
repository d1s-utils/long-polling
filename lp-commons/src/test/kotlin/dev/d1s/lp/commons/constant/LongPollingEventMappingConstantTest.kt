package dev.d1s.lp.commons.constant

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class LongPollingEventMappingConstantTest {

    @Test
    fun `should return valid mapping for getting events by group`() {
        expectThat(GET_EVENTS_BY_GROUP_MAPPING) isEqualTo "/events/{group}"
    }

    @Test
    fun `should return valid mapping for getting events by principal`() {
        expectThat(GET_EVENTS_BY_PRINCIPAL_MAPPING) isEqualTo "/events/{group}/{principal}"
    }
}
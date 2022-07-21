/*
 * Copyright 2022 Mikhail Titov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.d1s.lp.commons.test

import dev.d1s.lp.commons.entity.LongPollingEvent
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
            VALID_STUB,
            VALID_STUB,
            mutableSetOf(),
            Instant.EPOCH
        )
    }
}
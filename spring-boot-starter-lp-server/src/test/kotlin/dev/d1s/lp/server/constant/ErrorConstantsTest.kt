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

package dev.d1s.lp.server.constant

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class ErrorConstantsTest {

    @Test
    fun `should return valid error messages`() {
        expectThat(
            INCOMPATIBLE_EVENT_DATA_TYPE_ERROR
        ) isEqualTo "Event group must contain events with the same type of data."

        expectThat(
            UNAVAILABLE_EVENT_GROUP_ERROR
        ) isEqualTo "Event group is unavailable."
    }
}
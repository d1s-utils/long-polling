/*
 * Copyright 2022 Mikhail Titov and other contributors (if even present)
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

package dev.d1s.lp.web.testUtil

import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every

val eventSet = setOf(mockLongPollingEvent)

fun LongPollingEventService.prepare() {
    every {
        getByGroup(
            VALID_STUB,
            VALID_STUB
        )
    } returns eventSet

    every {
        getByPrincipal(
            VALID_STUB,
            VALID_STUB,
            VALID_STUB
        )
    } returns eventSet

    every {
        getAvailableGroups()
    } returns setOf(VALID_STUB)
}
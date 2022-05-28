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

package dev.d1s.lp.client.registry

import dev.d1s.lp.client.listener.ListenerGroup
import dev.d1s.lp.client.registry.impl.LongPollingEventListenerRegistryImpl
import dev.d1s.lp.client.testUtil.mockLongPollingEventListener
import dev.d1s.teabag.testing.constant.VALID_STUB
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class LongPollingEventListenerRegistryImplTest {

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
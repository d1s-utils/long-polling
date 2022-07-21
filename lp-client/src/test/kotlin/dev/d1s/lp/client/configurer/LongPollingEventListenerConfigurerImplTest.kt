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

package dev.d1s.lp.client.configurer

import dev.d1s.lp.client.configurer.impl.LongPollingEventListenerConfigurerImpl
import dev.d1s.lp.client.testUtil.mockEventPoller
import dev.d1s.lp.client.testUtil.mockLongPollingEventListener
import dev.d1s.lp.client.testUtil.mockLongPollingEventListenerRegistry
import dev.d1s.teabag.testing.assertDoesNotThrowBlocking
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.coVerify
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class LongPollingEventListenerConfigurerImplTest {

    private val longPollingEventListenerRegistry =
        mockLongPollingEventListenerRegistry

    private val eventPoller =
        mockEventPoller

    private val configurer = LongPollingEventListenerConfigurerImpl(
        longPollingEventListenerRegistry,
        eventPoller
    )

    @Test
    fun `should configure the listener`() {
        val listener = mockLongPollingEventListener

        assertDoesNotThrowBlocking {
            configurer.configureListener(VALID_STUB, VALID_STUB, Any::class.java, listener)
        }

        verify {
            longPollingEventListenerRegistry[VALID_STUB, VALID_STUB, Any::class.java] = listener
        }

        coVerify {
            eventPoller.startPolling(VALID_STUB, VALID_STUB, Any::class.java)
        }
    }
}
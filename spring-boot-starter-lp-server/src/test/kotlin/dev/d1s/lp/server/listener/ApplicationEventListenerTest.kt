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

package dev.d1s.lp.server.listener

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [ApplicationEventListener::class])
internal class ApplicationEventListenerTest {

    @Autowired
    private lateinit var applicationEventListener: ApplicationEventListener

    @MockkBean(relaxed = true)
    private lateinit var longPollingEventService: LongPollingEventService

    private val event = mockLongPollingEvent

    @Test
    fun `should intercept the event`() {
        assertDoesNotThrow {
            applicationEventListener.interceptEvent(event)
        }

        verify {
            longPollingEventService.add(event)
        }
    }
}
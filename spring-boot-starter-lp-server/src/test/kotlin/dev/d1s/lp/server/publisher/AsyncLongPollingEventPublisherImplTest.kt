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

package dev.d1s.lp.server.publisher

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.lp.server.publisher.impl.AsyncLongPollingEventPublisherImpl
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(classes = [AsyncLongPollingEventPublisherImpl::class])
internal class AsyncLongPollingEventPublisherImplTest {

    @Autowired
    private lateinit var publisher: AsyncLongPollingEventPublisherImpl

    @MockkBean
    private lateinit var longPollingEventService: LongPollingEventService

    @BeforeEach
    fun setup() {
        every {
            longPollingEventService.add(any())
        } returns setOf()
    }

    @Test
    fun `should publish event`() {
        val event = assertDoesNotThrow {
            publisher.publish(VALID_STUB, VALID_STUB, VALID_STUB)
        }.get()

        expectThat(event.group) isEqualTo VALID_STUB
        expectThat(event.principal) isEqualTo VALID_STUB
        expectThat(event.data) isEqualTo VALID_STUB
    }
}
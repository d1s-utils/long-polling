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

package dev.d1s.lp.web.controller.exception.mapper

import dev.d1s.advice.domain.ErrorResponseData
import dev.d1s.lp.server.exception.EventGroupNotFoundException
import dev.d1s.lp.web.exception.mapper.EventGroupNotFoundExceptionMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(classes = [EventGroupNotFoundExceptionMapper::class])
internal class EventGroupNotFoundExceptionMapperTest {

    @Autowired
    private lateinit var mapper: EventGroupNotFoundExceptionMapper

    @Test
    fun `should map EventGroupNotFoundException to valid response`() {
        val testException = EventGroupNotFoundException()
        expectThat(
            mapper.map(testException)
        ) isEqualTo ErrorResponseData(
            HttpStatus.NOT_FOUND,
            testException.message!!
        )
    }
}
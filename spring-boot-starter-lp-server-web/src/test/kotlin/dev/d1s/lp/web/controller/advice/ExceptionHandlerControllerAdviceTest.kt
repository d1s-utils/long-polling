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

package dev.d1s.lp.web.controller.advice

import dev.d1s.advice.exception.HttpStatusException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(classes = [ExceptionHandlerControllerAdvice::class])
internal class ExceptionHandlerControllerAdviceTest {

    @Autowired
    private lateinit var exceptionHandlerControllerAdvice: ExceptionHandlerControllerAdvice

    @Test
    fun `should re-throw EventGroupNotFoundException as HttpStatusException`() {
        val exception = assertThrows<HttpStatusException> {
            exceptionHandlerControllerAdvice.handleEventGroupNotFoundException()
        }

        expectThat(exception.status) isEqualTo HttpStatus.NOT_FOUND
        expectThat(exception.message) isEqualTo "The provided group was not found."
    }
}
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

package dev.d1s.lp.web.exception.mapper

import dev.d1s.advice.entity.ErrorResponseData
import dev.d1s.lp.server.constant.INCOMPATIBLE_EVENT_DATA_TYPE_ERROR
import dev.d1s.lp.server.exception.IncompatibleEventDataTypeException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest(
    classes = [IncompatibleEventDataTypeExceptionMapper::class],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
internal class IncompatibleEventDataTypeExceptionMapperTest {

    @set:Autowired
    lateinit var mapper: IncompatibleEventDataTypeExceptionMapper

    @Test
    fun `should map IncompatibleEventDataTypeException to valid response`() {
        expectThat(
            mapper.map(IncompatibleEventDataTypeException)
        ) isEqualTo ErrorResponseData(
            HttpStatus.BAD_REQUEST,
            INCOMPATIBLE_EVENT_DATA_TYPE_ERROR
        )
    }
}
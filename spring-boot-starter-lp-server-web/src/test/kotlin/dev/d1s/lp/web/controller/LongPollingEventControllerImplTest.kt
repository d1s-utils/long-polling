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

package dev.d1s.lp.web.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENT_GROUPS_MAPPING
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.lp.web.controller.impl.LongPollingEventControllerImpl
import dev.d1s.lp.web.testUtil.eventSet
import dev.d1s.lp.web.testUtil.prepare
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.*

@ContextConfiguration(
    classes = [
        LongPollingEventControllerImpl::class,
        JacksonAutoConfiguration::class
    ]
)
@WebMvcTest(
    controllers = [LongPollingEventControllerImpl::class],
    excludeAutoConfiguration = [SecurityAutoConfiguration::class]
)
class LongPollingEventControllerImplTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var longPollingEventService: LongPollingEventService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        longPollingEventService.prepare()
    }

    @Test
    fun `should get event set by group`() {
        mockMvc.get(
            GET_EVENTS_BY_GROUP_MAPPING,
            VALID_STUB
        ) {
            setRecipient()
        }.andExpect {

            expectEventSet()
        }

        verify {
            longPollingEventService.getByGroup(
                VALID_STUB,
                VALID_STUB
            )
        }
    }

    @Test
    fun `should get event set by principal`() {
        mockMvc.get(
            GET_EVENTS_BY_PRINCIPAL_MAPPING,
            VALID_STUB,
            VALID_STUB
        ) {
            setRecipient()
        }.andExpect {

            expectEventSet()
        }

        verify {
            longPollingEventService.getByPrincipal(
                VALID_STUB,
                VALID_STUB,
                VALID_STUB
            )
        }
    }

    @Test
    fun `should get available event groups`() {
        mockMvc.get(
            GET_EVENT_GROUPS_MAPPING
        ).andExpect {

            status {
                isOk()
            }

            content {
                json(
                    objectMapper.writeValueAsString(
                        setOf(VALID_STUB)
                    )
                )
            }
        }

        verify {
            longPollingEventService.getAvailableGroups()
        }
    }

    private fun MockMvcResultMatchersDsl.expectEventSet() {
        status {
            isOk()
        }

        content {
            json(
                objectMapper.writeValueAsString(eventSet)
            )
        }
    }

    private fun MockHttpServletRequestDsl.setRecipient() {
        param("recipient", VALID_STUB)
    }
}
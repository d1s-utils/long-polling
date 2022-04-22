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

import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.teabag.web.dto.ErrorDto
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.constraints.NotBlank

@Validated
@Tag(name = "Events", description = "Access the events.")
public interface LongPollingEventController {

    @GetMapping(GET_EVENTS_BY_GROUP_MAPPING)
    @Operation(
        summary = "Find events by group.",
        description = "Returns a collection of live events that were recently pushed.",
        responses = [
            ApiResponse(
                description = "Found group and it's events.",
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(
                            schema = Schema(implementation = LongPollingEvent::class),
                            uniqueItems = true
                        )
                    )
                ]
            ),
            ApiResponse(
                description = "Group was not found.",
                responseCode = "404",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorDto::class)
                    )
                ]
            )
        ]
    )
    public fun getByGroup(@PathVariable @NotBlank group: String): Set<LongPollingEvent<*>>

    @GetMapping(GET_EVENTS_BY_PRINCIPAL_MAPPING)
    @Operation(
        summary = "Find events by group and principal.",
        description = "Returns a collection of live events that were recently pushed. " +
                "Note that an empty collection will be returned if there were no events matching the principal.",
        responses = [
            ApiResponse(
                description = "Found group and it's events matching the provided principal.",
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(
                            schema = Schema(implementation = LongPollingEvent::class),
                            uniqueItems = true
                        )
                    )
                ]
            ),
            ApiResponse(
                description = "Group was not found.",
                responseCode = "404",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorDto::class)
                    )
                ]
            )
        ]
    )
    public fun getByPrincipal(
        @PathVariable @NotBlank group: String,
        @PathVariable @NotBlank principal: String
    ): Set<LongPollingEvent<*>>
}
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
import dev.d1s.lp.commons.constant.GET_EVENT_GROUPS_MAPPING
import dev.d1s.lp.commons.entity.LongPollingEvent
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotBlank

@Validated
public interface LongPollingEventController {

    @GetMapping(GET_EVENTS_BY_GROUP_MAPPING)
    public fun getByGroup(
        @PathVariable
        @NotBlank
        group: String,
        @RequestParam
        @NotBlank
        recipient: String
    ): Set<LongPollingEvent<*>>

    @GetMapping(GET_EVENTS_BY_PRINCIPAL_MAPPING)
    public fun getByPrincipal(
        @PathVariable
        @NotBlank
        group: String,
        @PathVariable
        @NotBlank
        principal: String,
        @RequestParam
        @NotBlank
        recipient: String
    ): Set<LongPollingEvent<*>>

    @GetMapping(GET_EVENT_GROUPS_MAPPING)
    public fun getEventGroups(): Set<String>
}
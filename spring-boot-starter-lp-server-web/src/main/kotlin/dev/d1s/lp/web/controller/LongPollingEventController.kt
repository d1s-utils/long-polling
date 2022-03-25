package dev.d1s.lp.web.controller

import dev.d1s.lp.commons.constant.GET_EVENTS_BY_GROUP_MAPPING
import dev.d1s.lp.commons.constant.GET_EVENTS_BY_PRINCIPAL_MAPPING
import dev.d1s.lp.commons.domain.LongPollingEvent
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.validation.constraints.NotBlank

@Validated
public interface LongPollingEventController {

    @GetMapping(GET_EVENTS_BY_GROUP_MAPPING)
    public fun getByGroup(@PathVariable @NotBlank group: String): Set<LongPollingEvent<*>>

    @GetMapping(GET_EVENTS_BY_PRINCIPAL_MAPPING)
    public fun getByPrincipal(
        @PathVariable @NotBlank group: String,
        @PathVariable @NotBlank principal: String
    ): Set<LongPollingEvent<*>>
}
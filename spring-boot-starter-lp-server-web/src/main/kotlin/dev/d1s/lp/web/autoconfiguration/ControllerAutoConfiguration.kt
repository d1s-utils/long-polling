package dev.d1s.lp.web.autoconfiguration

import dev.d1s.lp.web.controller.impl.LongPollingEventControllerImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class ControllerAutoConfiguration {

    @Bean
    internal fun longPollingEventController() = LongPollingEventControllerImpl()
}
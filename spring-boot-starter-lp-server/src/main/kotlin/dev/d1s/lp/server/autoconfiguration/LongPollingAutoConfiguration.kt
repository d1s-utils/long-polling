package dev.d1s.lp.server.autoconfiguration

import dev.d1s.lp.server.listener.ApplicationEventListener
import dev.d1s.lp.server.service.LongPollingEventService
import dev.d1s.lp.server.service.impl.LongPollingEventServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
public class LongPollingAutoConfiguration {

    @Bean
    internal fun longPollingEventService(): LongPollingEventService = LongPollingEventServiceImpl()

    @Bean
    internal fun applicationEventListener() = ApplicationEventListener()
}
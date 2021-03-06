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

package dev.d1s.lp.server.autoconfiguration

import dev.d1s.lp.server.listener.ApplicationEventListener
import dev.d1s.lp.server.publisher.impl.AsyncLongPollingEventPublisherImpl
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

    @Bean
    internal fun asyncLongPollingEventPublisher() = AsyncLongPollingEventPublisherImpl()
}
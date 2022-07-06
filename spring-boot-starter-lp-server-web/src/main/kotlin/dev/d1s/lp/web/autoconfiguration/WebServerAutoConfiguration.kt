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

package dev.d1s.lp.web.autoconfiguration

import dev.d1s.advice.mapper.ExceptionMapper
import dev.d1s.lp.web.controller.impl.LongPollingEventControllerImpl
import dev.d1s.lp.web.exception.mapper.IncompatibleEventDataTypeExceptionMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class WebServerAutoConfiguration {

    @Bean
    internal fun longPollingEventController() =
        LongPollingEventControllerImpl()

    @Bean
    internal fun incompatibleEventDataTypeExceptionMapper(): ExceptionMapper =
        IncompatibleEventDataTypeExceptionMapper()
}
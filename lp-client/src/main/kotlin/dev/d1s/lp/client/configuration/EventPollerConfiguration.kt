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

package dev.d1s.lp.client.configuration

import dev.d1s.lp.client.strategy.DelayCalculationStrategy
import dev.d1s.lp.client.strategy.FallbackStrategy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.time.Duration

public data class EventPollerConfiguration(
    var recipient: String? = null,
    var baseUrl: String? = null,
    var authorization: String? = null,
    var delayCalculationStrategy: DelayCalculationStrategy = DelayCalculationStrategy.Adjusted,
    var fallbackStrategy: FallbackStrategy = FallbackStrategy.Retry(
        Duration.ofSeconds(5)
    ),
    var coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
)
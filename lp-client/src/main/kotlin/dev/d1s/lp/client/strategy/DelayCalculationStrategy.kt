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

package dev.d1s.lp.client.strategy

import java.time.Duration
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

public sealed class DelayCalculationStrategy {

    internal abstract val delay: Duration

    public class Fixed(override val delay: Duration) : DelayCalculationStrategy()

    public object Adjusted : DelayCalculationStrategy() {

        private const val MAX_MEASUREMENTS_SIZE = 100

        private val measurements: MutableList<Long> = mutableListOf()

        override val delay: Duration
            get() = Duration.ofMillis(measurements.average().toLong())

        public fun <R : Any> measure(block: () -> R): R {
            var result: R by Delegates.notNull()

            if (measurements.size > MAX_MEASUREMENTS_SIZE) {
                repeat(measurements.size - MAX_MEASUREMENTS_SIZE) {
                    measurements.removeFirst()
                }
            }

            measurements += measureTimeMillis {
                result = block()
            }

            return result
        }
    }
}

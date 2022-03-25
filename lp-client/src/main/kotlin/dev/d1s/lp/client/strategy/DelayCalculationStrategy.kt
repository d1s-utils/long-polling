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

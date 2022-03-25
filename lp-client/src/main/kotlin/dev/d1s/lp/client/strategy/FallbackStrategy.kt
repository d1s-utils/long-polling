package dev.d1s.lp.client.strategy

import java.time.Duration

public sealed class FallbackStrategy {

    public object Fail : FallbackStrategy()

    public data class Retry(public val retryDelay: Duration) : FallbackStrategy()
}

package dev.d1s.lp.commons.domain

import java.time.Instant

public data class LongPollingEvent<T : Any>(
    val group: String,
    val timestamp: Instant,
    val principal: String,
    val data: T
)
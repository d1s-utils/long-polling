package dev.d1s.lp.client.listener

import dev.d1s.lp.commons.domain.LongPollingEvent

public typealias LongPollingEventListener<T> = LongPollingEvent<T>.() -> Unit
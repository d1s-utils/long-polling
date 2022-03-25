package dev.d1s.lp.commons.test

import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.teabag.testing.constant.VALID_STUB
import java.time.Instant

public val mockLongPollingEvent: LongPollingEvent<String>
    get() = LongPollingEvent(
        VALID_STUB,
        Instant.EPOCH,
        VALID_STUB,
        VALID_STUB
    )
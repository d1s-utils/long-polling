package dev.d1s.lp.server.listener

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.lp.server.service.LongPollingEventService
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration(classes = [ApplicationEventListener::class])
internal class ApplicationEventListenerTest {

    @Autowired
    private lateinit var applicationEventListener: ApplicationEventListener

    @MockkBean(relaxed = true)
    private lateinit var longPollingEventService: LongPollingEventService

    private val event = mockLongPollingEvent

    @Test
    fun `should intercept the event`() {
        assertDoesNotThrow {
            applicationEventListener.interceptEvent(event)
        }

        verify {
            longPollingEventService.add(event)
        }
    }
}
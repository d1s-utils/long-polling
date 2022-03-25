package dev.d1s.lp.server.service

import com.ninjasquad.springmockk.MockkBean
import dev.d1s.lp.commons.domain.LongPollingEvent
import dev.d1s.lp.commons.test.mockLongPollingEvent
import dev.d1s.lp.server.constant.LONG_POLLING_EVENT_CACHE
import dev.d1s.lp.server.exception.IncompatibleEventDataTypeException
import dev.d1s.lp.server.properties.LongPollingEventServerConfigurationProperties
import dev.d1s.lp.server.service.impl.LongPollingEventServiceImpl
import dev.d1s.teabag.testing.constant.VALID_STUB
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.containsExactly
import java.time.Duration
import java.time.Instant

@SpringBootTest
@ContextConfiguration(classes = [LongPollingEventServiceImpl::class])
internal class LongPollingEventServiceImplTest {

    @Autowired
    private lateinit var longPollingEventServiceImpl: LongPollingEventServiceImpl

    @MockkBean
    private lateinit var cacheManager: CacheManager

    @MockkBean
    private lateinit var properties: LongPollingEventServerConfigurationProperties

    private val event = mockLongPollingEvent

    private val mockCache = mockk<Cache>(relaxed = true)

    @BeforeEach
    fun setup() {
        every {
            cacheManager.getCache(LONG_POLLING_EVENT_CACHE)
        } returns mockCache

        every {
            mockCache[VALID_STUB]
        } returns Cache.ValueWrapper {
            mutableSetOf(event)
        }

        every {
            properties.eventLifeTime
        } returns Duration.ofDays(Integer.MAX_VALUE.toLong())
    }

    @Test
    fun `should add the event and return the correct set`() {
        every {
            mockCache[VALID_STUB]
        } returns Cache.ValueWrapper {
            mutableSetOf<LongPollingEvent<*>>()
        }

        val set = assertDoesNotThrow {
            longPollingEventServiceImpl.add(event)
        }

        expectThat(set).containsExactly(event)
    }

    @Test
    fun `should throw IncompatibleEventDataTypeException`() {
        assertThrows<IncompatibleEventDataTypeException> {
            longPollingEventServiceImpl.add(
                LongPollingEvent(VALID_STUB, Instant.EPOCH, VALID_STUB, Any())
            )
        }
    }

    @Test
    fun `should get event by group`() {
        val set = assertDoesNotThrow {
            longPollingEventServiceImpl.getByGroup(VALID_STUB)
        }

        expectThat(set).containsExactly(event)
    }

    @Test
    fun `should get the eveny by group and principal`() {
        val set = assertDoesNotThrow {
            longPollingEventServiceImpl.getByPrincipal(VALID_STUB, VALID_STUB)
        }

        expectThat(set).containsExactly(event)
    }
}
package dev.d1s.lp.web.controller.advice

import dev.d1s.advice.exception.HttpStatusException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@SpringBootTest
@ContextConfiguration(classes = [ExceptionHandlerControllerAdvice::class])
internal class ExceptionHandlerControllerAdviceTest {

    @Autowired
    private lateinit var exceptionHandlerControllerAdvice: ExceptionHandlerControllerAdvice

    @Test
    fun `should re-throw EventGroupNotFoundException as HttpStatusException`() {
        val exception = assertThrows<HttpStatusException> {
            exceptionHandlerControllerAdvice.handleEventGroupNotFoundException()
        }

        expectThat(exception.status) isEqualTo HttpStatus.NOT_FOUND
        expectThat(exception.message) isEqualTo "The provided group was not found."
    }
}
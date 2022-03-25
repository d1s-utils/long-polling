package dev.d1s.lp.web.controller.advice

import dev.d1s.advice.exception.HttpStatusException
import dev.d1s.lp.server.exception.EventGroupNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(EventGroupNotFoundException::class)
    internal fun handleEventGroupNotFoundException() {
        throw HttpStatusException(HttpStatus.NOT_FOUND, "The provided group was not found.")
    }
}
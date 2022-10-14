package com.siso.web.handler

import com.siso.exception.ConflictException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.CONFLICT
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [ConflictException::class, ExceptionHandler::class])
class ConflictExceptionHandler :
    ExceptionHandler<ConflictException?, HttpResponse<Any>> {
    override fun handle(request: HttpRequest<*>?, exception: ConflictException?): HttpResponse<Any> {
        if (exception != null) {
            return HttpResponse.status<Any?>(CONFLICT).body(ErrorMessageResponse(exception.message!!))
        }
        return HttpResponse.status<Any?>(CONFLICT)
    }
}
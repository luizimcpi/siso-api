package com.siso.web.handler

import com.siso.exception.BadRequestException
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton
@Requires(classes = [BadRequestException::class, ExceptionHandler::class])
class BadRequestExceptionHandler :
    ExceptionHandler<BadRequestException?, HttpResponse<ErrorMessageResponse>> {
    override fun handle(request: HttpRequest<*>?, exception: BadRequestException?): HttpResponse<ErrorMessageResponse> {
        if (exception != null) {
            return HttpResponse.badRequest(ErrorMessageResponse(exception.message!!))
        }
        return HttpResponse.badRequest()
    }
}
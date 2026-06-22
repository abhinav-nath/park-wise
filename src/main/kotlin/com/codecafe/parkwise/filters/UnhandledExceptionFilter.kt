package com.codecafe.parkwise.filters

import com.codecafe.parkwise.exceptions.BadRequestException
import com.codecafe.parkwise.exceptions.ConflictException
import com.codecafe.parkwise.models.error.asHttpResponse
import com.codecafe.parkwise.models.error.internalServerErrorProblem
import mu.KotlinLogging
import org.http4k.core.Filter
import org.http4k.core.HttpHandler

object UnhandledExceptionFilter : Filter {

    private val logger = KotlinLogging.logger {}

    @Suppress("TooGenericExceptionCaught")
    override fun invoke(next: HttpHandler): HttpHandler = { request ->
        try {
            next(request)
        } catch (ex: BadRequestException) {
            ex.asHttpResponse()
        } catch (ex: ConflictException) {
            ex.asHttpResponse()
        } catch (ex: Exception) {
            logger.error(ex) { "Unhandled exception" }
            internalServerErrorProblem(
                ex.message ?: "unknown exception '${ex.javaClass.canonicalName}'"
            ).asHttpResponse()
        }
    }
}

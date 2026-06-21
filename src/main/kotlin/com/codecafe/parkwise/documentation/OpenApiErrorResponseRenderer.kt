package com.codecafe.parkwise.documentation

import com.codecafe.parkwise.models.error.asHttpResponse
import com.codecafe.parkwise.models.error.badRequestProblem
import com.codecafe.parkwise.models.error.missingFieldProblem
import com.codecafe.parkwise.models.error.missingRequestQueryParameter
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import org.http4k.contract.ErrorResponseRenderer
import org.http4k.contract.JsonErrorResponseRenderer
import org.http4k.core.Response
import org.http4k.format.Jackson
import org.http4k.lens.LensFailure

object OpenApiErrorResponseRenderer : ErrorResponseRenderer {

    private val jsonErrorResponseRenderer = JsonErrorResponseRenderer(Jackson)

    override fun badRequest(lensFailure: LensFailure): Response =
        when (val cause = lensFailure.cause) {
            is MismatchedInputException -> {
                val field = cause.path.mapNotNull { it.fieldName }.joinToString(".")
                missingFieldProblem(field).asHttpResponse()
            }

            is JsonParseException -> {
                badRequestProblem(cause.originalMessage).asHttpResponse()
            }

            is ValueInstantiationException -> {
                badRequestProblem(cause.message ?: "invalid request payload").asHttpResponse()
            }

            else -> lensFailure.handleUnknownErrorInRequest()
        }

    override fun notFound(): Response =
        jsonErrorResponseRenderer.notFound()

    private fun LensFailure.handleUnknownErrorInRequest(): Response =
        if (isMissingQueryParameter()) {
            missingRequestQueryParameter(getParameter()).asHttpResponse()
        } else {
            jsonErrorResponseRenderer.badRequest(this)
        }

    private fun LensFailure.isMissingQueryParameter(): Boolean =
        failures.firstOrNull()?.meta?.location == "query"

    private fun LensFailure.getParameter(): String =
        failures.firstOrNull()?.meta?.name ?: "unknown"
}

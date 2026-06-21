package com.codecafe.parkwise.handlers.filters

import com.codecafe.parkwise.documentation.isDocumentationPath
import com.codecafe.parkwise.environment.DeployedEnvironment
import com.codecafe.parkwise.environment.DeployedEnvironment.PROD
import mu.KLogger
import mu.KotlinLogging
import net.logstash.logback.argument.StructuredArguments
import net.logstash.logback.argument.StructuredArguments.kv
import org.http4k.appendIfNotBlank
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.slf4j.event.Level
import java.time.Clock
import java.time.Duration

private const val ERROR_CODE_500 = 500

class NetworkLoggingFilter(
    private val clock: Clock,
    private val deployedEnvironment: DeployedEnvironment,
    private val direction: RequestDirection,
    defaultLogger: KLogger? = null
) : Filter {

    private val logger = defaultLogger ?: KotlinLogging.logger {}

    private val defaultHeaders = listOf(
        "user-agent",
        "x-envoy-attempt-count",
        "x-forwarded-for",
        "x-forwarded-host",
        "x-original-forwarded-for",
        "x-correlation-id",
        "x-request-id"
    )

    override fun invoke(next: HttpHandler): HttpHandler = { request ->
        val start = clock.instant()
        next(request).also { response ->
            if (!request.uri.path.isDocumentationPath()) {
                val end = clock.instant()
                logResponse(request, response, Duration.between(start, end))
            }
        }
    }

    @Suppress("SpreadOperator")
    private fun logResponse(request: Request, response: Response, duration: Duration) {
        val headersToLog = request.headers.toMap()
            .filter { (name, _) -> name.lowercase() in defaultHeaders }
            .map { (name, value) -> kv("header.$name", value) }

        val commonFields = listOf(
            kv("request_uri", request.uri.path),
            kv("request_method", request.method.name),
            kv("response_status_code", response.status.code),
            kv("duration_ms", duration.toMillis())
        )

        val maybeRequestBody = listOf<StructuredArguments>().run {
            if (deployedEnvironment != PROD) {
                plus(kv("request_body", request.bodyString()))
            } else {
                this
            }
        }

        val allFieldsToLog = commonFields + headersToLog + maybeRequestBody

        logger.atLevel(logLevel(response))
            .log(getLogMessage(request, response), *allFieldsToLog.toTypedArray())
    }

    private fun logLevel(response: Response): Level =
        when {
            response.status.successful -> Level.INFO
            response.status.code < ERROR_CODE_500 -> Level.WARN
            else -> Level.ERROR
        }

    private fun getLogMessage(request: Request, response: Response): String =
        StringBuilder()
            .append("${direction.name.lowercase()} request: ${request.method} ${request.uri.path}")
            .appendIfNotBlank(request.uri.query, "?", request.uri.query)
            .append(" -> ${direction.action()} ${response.status}")
            .toString()

    private fun RequestDirection.action() = when (this) {
        RequestDirection.INBOUND -> "returning"
        RequestDirection.OUTBOUND -> "returned"
    }
}

enum class RequestDirection {
    INBOUND,
    OUTBOUND
}

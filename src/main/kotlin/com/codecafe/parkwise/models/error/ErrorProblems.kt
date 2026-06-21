package com.codecafe.parkwise.models.error

import org.http4k.core.ContentType
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.lens.Header
import org.zalando.problem.Problem
import org.zalando.problem.Status.BAD_REQUEST
import org.zalando.problem.Status.INTERNAL_SERVER_ERROR
import org.zalando.problem.Status.NOT_FOUND

private val problemLens = BodyProblemLens.problemLens

object BodyProblemLens {
    val problemLens = org.http4k.core.Body.auto<Problem>().toLens()
}

fun Problem.asHttpResponse(): Response =
    Response(Status(status?.statusCode ?: Status.INTERNAL_SERVER_ERROR.code, title ?: "Error"))
        .with(problemLens of this)
        .with(Header.CONTENT_TYPE of ContentType.APPLICATION_JSON)

fun internalServerErrorProblem(detail: String): Problem =
    Problem.builder()
        .withTitle("Internal Server Error")
        .withStatus(INTERNAL_SERVER_ERROR)
        .withDetail(detail)
        .build()

fun badRequestProblem(detail: String): Problem =
    Problem.builder()
        .withTitle("Bad Request")
        .withStatus(BAD_REQUEST)
        .withDetail(detail)
        .build()

fun missingFieldProblem(field: String): Problem =
    Problem.builder()
        .withTitle("Bad Request")
        .withStatus(BAD_REQUEST)
        .withDetail("missing required fields")
        .with("field", field)
        .build()

fun missingRequestQueryParameter(parameter: String): Problem =
    Problem.builder()
        .withTitle("Bad Request")
        .withStatus(BAD_REQUEST)
        .withDetail("missing query parameter in request")
        .with("parameter", parameter)
        .build()

fun notFoundProblem(detail: String): Problem =
    Problem.builder()
        .withTitle("Not Found")
        .withStatus(NOT_FOUND)
        .withDetail(detail)
        .build()

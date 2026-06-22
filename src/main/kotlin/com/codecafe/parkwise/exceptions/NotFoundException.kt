package com.codecafe.parkwise.exceptions

import com.codecafe.parkwise.validation.ErrorResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status

open class NotFoundException(
    @JsonIgnore val error: ErrorResponse
) : AbstractThrowableProblem(
    null,
    "Not Found",
    Status.NOT_FOUND,
    error.message,
    null,
    null,
    mapOf(
        "code" to error.code,
        "field" to error.field
    )
) {
    override fun toString(): String =
        "NotFoundException(error=$error)"

    override fun getCause(): Exceptional? =
        super.cause
}

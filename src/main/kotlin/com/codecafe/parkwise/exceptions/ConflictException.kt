package com.codecafe.parkwise.exceptions

import com.codecafe.parkwise.validation.ErrorResponse
import com.fasterxml.jackson.annotation.JsonIgnore
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status

open class ConflictException(
    @JsonIgnore val error: ErrorResponse
) : AbstractThrowableProblem(
    null,
    "Conflict",
    Status.CONFLICT,
    error.message,
    null,
    null,
    mapOf(
        "code" to error.code,
        "field" to error.field
    )
) {
    override fun toString(): String =
        "ConflictException(error=$error)"

    override fun getCause(): Exceptional? =
        super.cause
}

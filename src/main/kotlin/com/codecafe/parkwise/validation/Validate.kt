package com.codecafe.parkwise.validation

import com.codecafe.parkwise.exceptions.BadRequestException
import kotlin.contracts.contract

inline fun validate(value: Boolean, lazyError: () -> ErrorResponse) {
    contract {
        returns() implies value
    }

    if (!value) {
        throw BadRequestException(lazyError())
    }
}

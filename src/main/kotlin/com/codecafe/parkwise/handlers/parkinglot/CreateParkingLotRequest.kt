package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.validation.ErrorResponse
import com.codecafe.parkwise.validation.ErrorResponse.Companion.INVALID_PAYLOAD
import com.codecafe.parkwise.validation.validate

data class CreateParkingLotRequest(
    val code: String?,
    val name: String?,
    val location: String?
) {
    init {
        validate(!code.isNullOrBlank()) {
            ErrorResponse(
                code = INVALID_PAYLOAD,
                field = "code",
                message = "code is required"
            )
        }

        validate(code.trim().matches(PARKING_LOT_CODE_REGEX)) {
            ErrorResponse(
                code = INVALID_PAYLOAD,
                field = "code",
                message = "code must contain only uppercase letters, numbers and hyphens"
            )
        }

        validate(!name.isNullOrBlank()) {
            ErrorResponse(
                code = INVALID_PAYLOAD,
                field = "name",
                message = "name is required"
            )
        }

        validate(!location.isNullOrBlank()) {
            ErrorResponse(
                code = INVALID_PAYLOAD,
                field = "location",
                message = "location is required"
            )
        }
    }

    companion object {
        private val PARKING_LOT_CODE_REGEX = Regex("^[A-Z0-9-]{3,50}$")

        val example = CreateParkingLotRequest(
            code = "PHOENIX-MARKETCITY-PUNE",
            name = "Phoenix Market City",
            location = "Pune"
        )
    }
}

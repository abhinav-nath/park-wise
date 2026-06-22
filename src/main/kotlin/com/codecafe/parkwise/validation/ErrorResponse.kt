package com.codecafe.parkwise.validation

data class ErrorResponse(
    val code: String,
    val field: String,
    val message: String
) {
    companion object {
        const val INVALID_PAYLOAD = "INVALID_PAYLOAD"
        const val PARKING_LOT_ALREADY_EXISTS = "PARKING_LOT_ALREADY_EXISTS"
    }
}

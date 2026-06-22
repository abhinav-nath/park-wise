package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.models.parkinglot.ParkingLot
import com.codecafe.parkwise.models.parkinglot.ParkingLotStatus
import java.time.Instant
import java.util.UUID

data class ParkingLotResponse(
    val id: UUID,
    val code: String,
    val name: String,
    val location: String,
    val status: ParkingLotStatus,
    val createdAt: Instant
) {
    companion object {
        val example = ParkingLotResponse(
            id = UUID.fromString("5cc16090-4bb1-4a06-845b-c109e94207bb"),
            code = "PHOENIX-MARKETCITY-PUNE",
            name = "Phoenix Market City",
            location = "Pune",
            status = ParkingLotStatus.ACTIVE,
            createdAt = Instant.parse("2026-06-22T16:30:00Z")
        )
    }
}

fun ParkingLot.toParkingLotResponse(): ParkingLotResponse =
    ParkingLotResponse(
        id = id,
        code = code,
        name = name,
        location = location,
        status = status,
        createdAt = createdAt
    )

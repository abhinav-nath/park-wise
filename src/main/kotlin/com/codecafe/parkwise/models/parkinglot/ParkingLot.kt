package com.codecafe.parkwise.models.parkinglot

import java.time.Instant
import java.util.UUID

data class ParkingLot(
    val id: UUID,
    val code: String,
    val name: String,
    val location: String,
    val status: ParkingLotStatus,
    val createdAt: Instant
)

enum class ParkingLotStatus {
    ACTIVE,
    INACTIVE
}

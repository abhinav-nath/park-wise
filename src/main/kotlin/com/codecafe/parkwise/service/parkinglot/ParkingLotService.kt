package com.codecafe.parkwise.service.parkinglot

import com.codecafe.parkwise.exceptions.ConflictException
import com.codecafe.parkwise.exceptions.NotFoundException
import com.codecafe.parkwise.models.parkinglot.ParkingLot
import com.codecafe.parkwise.models.parkinglot.ParkingLotStatus
import com.codecafe.parkwise.repository.parkinglot.ParkingLotRepository
import com.codecafe.parkwise.validation.ErrorResponse
import com.codecafe.parkwise.validation.ErrorResponse.Companion.PARKING_LOT_ALREADY_EXISTS
import com.codecafe.parkwise.validation.ErrorResponse.Companion.PARKING_LOT_NOT_FOUND
import java.time.Clock
import java.util.UUID

class ParkingLotService(
    private val parkingLotRepository: ParkingLotRepository,
    private val clock: Clock
) {
    fun createParkingLot(code: String, name: String, location: String): ParkingLot {
        val normalizedCode = code.trim().uppercase()
        val normalizedName = name.trim()
        val normalizedLocation = location.trim()

        if (parkingLotRepository.existsByCode(normalizedCode)) {
            throw ConflictException(
                ErrorResponse(
                    code = PARKING_LOT_ALREADY_EXISTS,
                    field = "code",
                    message = "parking lot already exists for code"
                )
            )
        }

        val parkingLot = ParkingLot(
            id = UUID.randomUUID(),
            code = normalizedCode,
            name = normalizedName,
            location = normalizedLocation,
            status = ParkingLotStatus.ACTIVE,
            createdAt = clock.instant()
        )

        return parkingLotRepository.save(parkingLot)
    }

    fun getParkingLot(parkingLotId: UUID): ParkingLot =
        parkingLotRepository.findById(parkingLotId)
            ?: throw NotFoundException(
                ErrorResponse(
                    code = PARKING_LOT_NOT_FOUND,
                    field = "parkingLotId",
                    message = "parking lot not found"
                )
            )
}

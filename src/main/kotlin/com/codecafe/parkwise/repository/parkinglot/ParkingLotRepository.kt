package com.codecafe.parkwise.repository.parkinglot

import com.codecafe.parkwise.models.parkinglot.ParkingLot
import java.util.UUID

interface ParkingLotRepository {
    fun save(parkingLot: ParkingLot): ParkingLot
    fun existsByCode(code: String): Boolean
    fun findById(id: UUID): ParkingLot?
}

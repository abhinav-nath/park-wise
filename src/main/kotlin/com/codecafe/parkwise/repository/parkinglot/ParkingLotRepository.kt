package com.codecafe.parkwise.repository.parkinglot

import com.codecafe.parkwise.models.parkinglot.ParkingLot

interface ParkingLotRepository {
    fun save(parkingLot: ParkingLot): ParkingLot
    fun existsByCode(code: String): Boolean
}

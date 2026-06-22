package com.codecafe.parkwise.repository.parkinglot

import com.codecafe.parkwise.models.parkinglot.ParkingLot
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryParkingLotRepository : ParkingLotRepository {

    private val parkingLots = ConcurrentHashMap<UUID, ParkingLot>()

    override fun save(parkingLot: ParkingLot): ParkingLot {
        parkingLots[parkingLot.id] = parkingLot
        return parkingLot
    }

    override fun existsByCode(code: String): Boolean =
        parkingLots.values.any { it.code == code }

    override fun findById(id: UUID): ParkingLot? =
        parkingLots[id]
}

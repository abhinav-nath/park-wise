package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.CustomHttp4kFormats.auto
import com.codecafe.parkwise.service.parkinglot.ParkingLotService
import org.http4k.contract.ContractRoute
import org.http4k.contract.Tag
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.lens.Path
import org.http4k.lens.uuid
import java.util.UUID

class GetParkingLotHandler(
    private val parkingLotService: ParkingLotService
) {
    private val parkingLotId = Path.uuid().of("parkingLotId", "Parking lot ID")
    private val responseLens = Body.auto<ParkingLotResponse>().toLens()

    fun contractRoute(): ContractRoute =
        ("v1/parking-lots" / parkingLotId) meta {
            tags += Tag("Parking Lots")
            summary = "Get parking lot by ID"
            description = "Returns a parking lot by its ID."
            produces += ContentType.APPLICATION_JSON
            returning(OK, responseLens to ParkingLotResponse.example)
        } bindContract Method.GET to { parkingLotId: UUID ->
            {
                    _: Request ->
                val parkingLot = parkingLotService.getParkingLot(parkingLotId)

                Response(OK).with(
                    responseLens of parkingLot.toParkingLotResponse()
                )
            }
        }
}

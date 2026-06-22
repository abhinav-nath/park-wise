package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.CustomHttp4kFormats.auto
import com.codecafe.parkwise.service.parkinglot.ParkingLotService
import org.http4k.contract.ContractRoute
import org.http4k.contract.Tag
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with

class CreateParkingLotHandler(
    private val parkingLotService: ParkingLotService
) {
    private val requestLens = Body.auto<CreateParkingLotRequest>().toLens()
    private val responseLens = Body.auto<ParkingLotResponse>().toLens()

    fun contractRoute(): ContractRoute =
        "v1/parking-lots" meta {
            tags += Tag("Parking Lots")
            summary = "Create parking lot"
            description = "Creates a new parking lot."
            consumes += ContentType.APPLICATION_JSON
            produces += ContentType.APPLICATION_JSON
            receiving(requestLens to CreateParkingLotRequest.example)
            returning(CREATED, responseLens to ParkingLotResponse.example)
        } bindContract Method.POST to { request: Request ->
            val createParkingLotRequest = requestLens(request)

            val parkingLot = parkingLotService.createParkingLot(
                code = createParkingLotRequest.code!!,
                name = createParkingLotRequest.name!!,
                location = createParkingLotRequest.location!!
            )

            Response(CREATED).with(
                responseLens of parkingLot.toParkingLotResponse()
            )
        }
}

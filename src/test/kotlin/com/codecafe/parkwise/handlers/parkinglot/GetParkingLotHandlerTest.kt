package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.models.parkinglot.ParkingLot
import com.codecafe.parkwise.models.parkinglot.ParkingLotStatus
import com.codecafe.parkwise.service.parkinglot.ParkingLotService
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.http4k.contract.contract
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Instant
import java.util.UUID

class GetParkingLotHandlerTest : FreeSpec({

    val parkingLotService = mockk<ParkingLotService>()
    val handler = GetParkingLotHandler(parkingLotService)

    val app = routes(
        "/" bind contract {
            routes += handler.contractRoute()
        }
    )

    afterTest {
        clearAllMocks()
    }

    "GetParkingLotHandler should" - {

        "return parking lot by ID" {
            val parkingLotId = UUID.fromString("5cc16090-4bb1-4a06-845b-c109e94207bb")

            every { parkingLotService.getParkingLot(parkingLotId) } returns ParkingLot(
                id = parkingLotId,
                code = "PHOENIX-MARKETCITY-PUNE",
                name = "Phoenix Market City",
                location = "Pune",
                status = ParkingLotStatus.ACTIVE,
                createdAt = Instant.parse("2026-06-22T16:30:00Z")
            )

            val response = app(
                Request(GET, "/v1/parking-lots/$parkingLotId")
            )

            response.status shouldBe OK
            response.bodyString() shouldBe
                """
                {"id":"5cc16090-4bb1-4a06-845b-c109e94207bb","code":"PHOENIX-MARKETCITY-PUNE","name":"Phoenix Market City","location":"Pune","status":"ACTIVE","createdAt":"2026-06-22T16:30:00Z"}
                """.trimIndent()

            verify(exactly = 1) {
                parkingLotService.getParkingLot(parkingLotId)
            }
        }
    }
})

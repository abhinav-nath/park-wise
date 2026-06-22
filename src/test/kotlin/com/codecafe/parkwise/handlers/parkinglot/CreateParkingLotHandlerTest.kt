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
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status.Companion.CREATED
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Instant
import java.util.UUID

class CreateParkingLotHandlerTest : FreeSpec({

    val parkingLotService = mockk<ParkingLotService>()
    val handler = CreateParkingLotHandler(parkingLotService)

    val app = routes(
        "/" bind contract {
            routes += handler.contractRoute()
        }
    )

    afterTest {
        clearAllMocks()
    }

    "CreateParkingLotHandler should" - {

        "create parking lot" {
            val parkingLotId = UUID.fromString("5cc16090-4bb1-4a06-845b-c109e94207bb")

            every {
                parkingLotService.createParkingLot(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            } returns ParkingLot(
                id = parkingLotId,
                code = "PHOENIX-MARKETCITY-PUNE",
                name = "Phoenix Market City",
                location = "Pune",
                status = ParkingLotStatus.ACTIVE,
                createdAt = Instant.parse("2026-06-22T16:30:00Z")
            )

            val response = app(
                Request(POST, "/v1/parking-lots")
                    .header("Content-Type", "application/json")
                    .body(
                        """
                        {
                          "code": "PHOENIX-MARKETCITY-PUNE",
                          "name": "Phoenix Market City",
                          "location": "Pune"
                        }
                        """.trimIndent()
                    )
            )

            response.status shouldBe CREATED
            response.bodyString() shouldBe
                """
                {"id":"5cc16090-4bb1-4a06-845b-c109e94207bb","code":"PHOENIX-MARKETCITY-PUNE","name":"Phoenix Market City","location":"Pune","status":"ACTIVE","createdAt":"2026-06-22T16:30:00Z"}
                """.trimIndent()

            verify(exactly = 1) {
                parkingLotService.createParkingLot(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }
        }
    }
})

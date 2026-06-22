package com.codecafe.parkwise.service.parkinglot

import com.codecafe.parkwise.exceptions.ConflictException
import com.codecafe.parkwise.models.parkinglot.ParkingLot
import com.codecafe.parkwise.models.parkinglot.ParkingLotStatus
import com.codecafe.parkwise.repository.parkinglot.ParkingLotRepository
import com.codecafe.parkwise.validation.ErrorResponse.Companion.PARKING_LOT_ALREADY_EXISTS
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class ParkingLotServiceTest : FreeSpec({

    val parkingLotRepository = mockk<ParkingLotRepository>()
    val clock = Clock.fixed(Instant.parse("2026-06-22T16:30:00Z"), ZoneOffset.UTC)
    val service = ParkingLotService(parkingLotRepository, clock)

    afterTest {
        clearAllMocks()
    }

    "createParkingLot should" - {

        "create parking lot when code does not already exist" {
            val parkingLotSlot = slot<ParkingLot>()

            every { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") } returns false
            every { parkingLotRepository.save(capture(parkingLotSlot)) } answers { parkingLotSlot.captured }

            val result = service.createParkingLot(
                code = "PHOENIX-MARKETCITY-PUNE",
                name = "Phoenix Market City",
                location = "Pune"
            )

            result.code shouldBe "PHOENIX-MARKETCITY-PUNE"
            result.name shouldBe "Phoenix Market City"
            result.location shouldBe "Pune"
            result.status shouldBe ParkingLotStatus.ACTIVE
            result.createdAt shouldBe Instant.parse("2026-06-22T16:30:00Z")

            verify(exactly = 1) { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") }
            verify(exactly = 1) { parkingLotRepository.save(any()) }
        }

        "trim name and location before saving" {
            val parkingLotSlot = slot<ParkingLot>()

            every { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") } returns false
            every { parkingLotRepository.save(capture(parkingLotSlot)) } answers { parkingLotSlot.captured }

            val result = service.createParkingLot(
                code = "PHOENIX-MARKETCITY-PUNE",
                name = "  Phoenix Market City  ",
                location = "  Pune  "
            )

            result.name shouldBe "Phoenix Market City"
            result.location shouldBe "Pune"

            verify(exactly = 1) { parkingLotRepository.save(any()) }
        }

        "normalize code before checking and saving" {
            val parkingLotSlot = slot<ParkingLot>()

            every { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") } returns false
            every { parkingLotRepository.save(capture(parkingLotSlot)) } answers { parkingLotSlot.captured }

            val result = service.createParkingLot(
                code = " phoenix-marketcity-pune ",
                name = "Phoenix Market City",
                location = "Pune"
            )

            result.code shouldBe "PHOENIX-MARKETCITY-PUNE"

            verify(exactly = 1) { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") }
            verify(exactly = 1) { parkingLotRepository.save(any()) }
        }

        "fail when parking lot code already exists" {
            every { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") } returns true

            val exception = shouldThrow<ConflictException> {
                service.createParkingLot(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe PARKING_LOT_ALREADY_EXISTS
            exception.error.field shouldBe "code"
            exception.error.message shouldBe "parking lot already exists for code"

            verify(exactly = 1) { parkingLotRepository.existsByCode("PHOENIX-MARKETCITY-PUNE") }
            verify(exactly = 0) { parkingLotRepository.save(any()) }
        }
    }
})

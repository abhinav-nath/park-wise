package com.codecafe.parkwise.handlers.parkinglot

import com.codecafe.parkwise.exceptions.BadRequestException
import com.codecafe.parkwise.validation.ErrorResponse.Companion.INVALID_PAYLOAD
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CreateParkingLotRequestTest : FreeSpec({

    "CreateParkingLotRequest should" - {

        "be created when all fields are valid" {
            val request = CreateParkingLotRequest(
                code = "PHOENIX-MARKETCITY-PUNE",
                name = "Phoenix Market City",
                location = "Pune"
            )

            request.code shouldBe "PHOENIX-MARKETCITY-PUNE"
            request.name shouldBe "Phoenix Market City"
            request.location shouldBe "Pune"
        }

        "fail when code is missing" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = null,
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "code"
            exception.error.message shouldBe "code is required"
        }

        "fail when code is blank" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = " ",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "code"
            exception.error.message shouldBe "code is required"
        }

        "fail when code contains lowercase letters" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "phoenix-marketcity-pune",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "code"
            exception.error.message shouldBe "code must contain only uppercase letters, numbers and hyphens"
        }

        "fail when code contains special characters" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "PHOENIX_MARKETCITY_PUNE",
                    name = "Phoenix Market City",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "code"
            exception.error.message shouldBe "code must contain only uppercase letters, numbers and hyphens"
        }

        "fail when name is missing" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = null,
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "name"
            exception.error.message shouldBe "name is required"
        }

        "fail when name is blank" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = " ",
                    location = "Pune"
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "name"
            exception.error.message shouldBe "name is required"
        }

        "fail when location is missing" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = "Phoenix Market City",
                    location = null
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "location"
            exception.error.message shouldBe "location is required"
        }

        "fail when location is blank" {
            val exception = shouldThrow<BadRequestException> {
                CreateParkingLotRequest(
                    code = "PHOENIX-MARKETCITY-PUNE",
                    name = "Phoenix Market City",
                    location = " "
                )
            }

            exception.error.code shouldBe INVALID_PAYLOAD
            exception.error.field shouldBe "location"
            exception.error.message shouldBe "location is required"
        }
    }
})

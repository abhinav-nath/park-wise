package com.codecafe.parkwise.handlers.status

import com.codecafe.parkwise.CustomHttp4kFormats.auto
import org.http4k.contract.ContractRoute
import org.http4k.contract.Tag
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import java.time.Clock

class StatusHandler(
    private val clock: Clock
) {
    private val responseLens = Body.auto<StatusResponse>().toLens()

    fun contractRoute(): ContractRoute =
        "v1/status" meta {
            tags += Tag("Status")
            summary = "Get ParkWise application status"
            description = "Returns basic application status."
            produces += ContentType.APPLICATION_JSON
            returning(OK, responseLens to StatusResponse.example)
        } bindContract Method.GET to { _: Request ->
            Response(OK).with(
                responseLens of StatusResponse(
                    name = "park-wise",
                    status = "UP",
                    timestamp = clock.instant().toString()
                )
            )
        }
}

data class StatusResponse(
    val name: String,
    val status: String,
    val timestamp: String
) {
    companion object {
        val example = StatusResponse(
            name = "park-wise",
            status = "UP",
            timestamp = "2026-06-20T00:00:00Z"
        )
    }
}

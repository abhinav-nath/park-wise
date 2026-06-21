package com.codecafe.parkwise.handlers.cloud

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

class HealthHandler {

    fun routes(): RoutingHttpHandler =
        routes(
            "/health" bind GET to { Response(OK).body("OK") },
            "/live" bind GET to { Response(OK).body("OK") },
            "/ready" bind GET to { Response(OK).body("OK") }
        )
}

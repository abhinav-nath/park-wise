package com.codecafe.parkwise

import com.codecafe.parkwise.documentation.OpenApiErrorResponseRenderer
import com.codecafe.parkwise.documentation.security.FakeBearerAuthSecurity
import com.codecafe.parkwise.environment.DeployedEnvironment.Companion.deployedEnvironment
import com.codecafe.parkwise.handlers.docs.ApiDocumentationHandler
import com.codecafe.parkwise.handlers.filters.NetworkLoggingFilter
import com.codecafe.parkwise.handlers.filters.RequestDirection.INBOUND
import com.codecafe.parkwise.handlers.status.StatusHandler
import org.http4k.config.Environment
import org.http4k.contract.ContractRoute
import org.http4k.contract.contract
import org.http4k.contract.jsonschema.v3.AutoJsonToJsonSchema
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.ApiRenderer
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Filter
import org.http4k.core.RequestContexts
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.format.Jackson
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Clock

const val BASE_PATH = "/parkwise"

fun application(environment: Environment): RoutingHttpHandler {
    val clock = Clock.systemUTC()
    val contexts = RequestContexts()

    val statusHandler = StatusHandler(clock)

    return ServerFilters.InitialiseRequestContext(contexts)
        .then(NetworkLoggingFilter(clock, environment.deployedEnvironment(), INBOUND))
        .then(
            routes(
                apiHandlers(
                    listOf(
                        statusHandler.contractRoute()
                    )
                ),
                ApiDocumentationHandler().apiDocumentation()
            )
        )
        .withFilter(commonResponseHeaders())
        .withBasePath(BASE_PATH)
}

private fun apiHandlers(contractRoutes: List<ContractRoute>) =
    routes(
        "/" bind contract {
            renderer = OpenApi3(
                apiInfo = ApiInfo(
                    title = "ParkWise",
                    version = "v1.0",
                    description = "APIs to manage parking lots, slots, vehicle entries, exits and payments."
                ),
                json = CustomHttp4kFormats,
                apiRenderer = ApiRenderer.Auto(CustomHttp4kFormats, AutoJsonToJsonSchema(Jackson)),
                errorResponseRenderer = OpenApiErrorResponseRenderer,
                servers = listOf(
                    ApiServer(
                        url = Uri.of("http://localhost:8080$BASE_PATH"),
                        description = "Local Environment"
                    )
                ),
                securityRenderer = FakeBearerAuthSecurity.renderer
            )

            descriptionPath = "/swagger.json"

            val bearer = FakeBearerAuthSecurity(token = "ignored", name = "bearerAuth")
            security = bearer

            routes += contractRoutes
        }
    )

private fun commonResponseHeaders() = Filter { next ->
    {
            request ->
        next(request)
            .header("X-Frame-Options", "DENY")
            .header("X-Content-Type-Options", "nosniff")
    }
}

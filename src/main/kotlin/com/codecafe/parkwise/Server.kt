package com.codecafe.parkwise

import com.codecafe.parkwise.filters.UnhandledExceptionFilter
import com.codecafe.parkwise.handlers.cloud.HealthHandler
import mu.KotlinLogging
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.core.then
import org.http4k.k8s.Http4kK8sServer
import org.http4k.routing.RoutingHttpHandler
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

private val logger = KotlinLogging.logger {}

fun runServer(environment: Environment): Http4kServer {
    val appPort = EnvironmentKey.k8s.SERVICE_PORT(environment)
    val healthPort = EnvironmentKey.k8s.HEALTH_PORT(environment)

    val healthServer = HealthHandler()
        .routes()
        .asServer(Jetty(healthPort))

    val appServer = monitoredServer(
        handler = application(environment),
        port = appPort
    )

    logger.info { "Starting ParkWise on port $appPort with health available on port $healthPort" }

    return Http4kK8sServer(appServer, healthServer).start()
}

private fun monitoredServer(
    handler: RoutingHttpHandler,
    port: Int
): Http4kServer =
    UnhandledExceptionFilter
        .then(handler)
        .asServer(Jetty(port))

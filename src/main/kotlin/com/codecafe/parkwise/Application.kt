package com.codecafe.parkwise

import mu.KotlinLogging
import org.http4k.config.Environment
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

fun main() {
    runCatching {
        val environment = Environment.ENV overrides Environment.fromResource("app.properties")
        val localEnv = runCatching {
            Environment.fromResource("local.properties")
        }.getOrNull()

        runServer(localEnv?.let { it overrides environment } ?: environment)
    }.onFailure {
        logger.error(it) { "Startup failed" }
        exitProcess(1)
    }
}

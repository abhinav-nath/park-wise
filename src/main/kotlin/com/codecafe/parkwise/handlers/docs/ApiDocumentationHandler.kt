package com.codecafe.parkwise.handlers.docs

import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

class ApiDocumentationHandler {

    fun apiDocumentation() = routes(
        "/docs" bind static(ResourceLoader.Classpath("/swagger-ui")),
        "/resources/swagger-ui" bind static(ResourceLoader.Classpath("/swagger-ui"))
    )
}

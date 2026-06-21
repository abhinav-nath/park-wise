package com.codecafe.parkwise

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.StringBiDiMappings.uri
import org.zalando.problem.jackson.ProblemModule
import java.time.format.DateTimeFormatter

val mapper: ObjectMapper = KotlinModule.Builder()
    .build()
    .asConfigurable()
    .text(StringBiDiMappings.localDate(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
    .text(StringBiDiMappings.localDateTime(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
    .text(uri())
    .done()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .registerModule(ProblemModule())
    .registerModule(JavaTimeModule())

object CustomHttp4kFormats : ConfigurableJackson(mapper)

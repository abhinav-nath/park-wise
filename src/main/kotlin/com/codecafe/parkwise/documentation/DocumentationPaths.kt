package com.codecafe.parkwise.documentation

import com.codecafe.parkwise.BASE_PATH

fun String.isDocumentationPath(): Boolean =
    documentationPaths.any { contains(it) }

private val documentationPaths = listOf(
    "/favicon",
    "$BASE_PATH/docs",
    "$BASE_PATH/resources",
    "$BASE_PATH/swagger"
)

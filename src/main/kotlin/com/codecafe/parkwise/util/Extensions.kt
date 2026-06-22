package com.codecafe.parkwise.util

object Extensions {
    val Throwable.rootCause: Throwable
        get() {
            var rootCause = this
            while (rootCause.cause != null) {
                rootCause = rootCause.cause!!
            }
            return rootCause
        }
}

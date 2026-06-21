package com.codecafe.parkwise.environment

import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.lens.string

enum class DeployedEnvironment {
    LOCAL,
    DEV,
    QA,
    PROD;

    companion object {
        private val deployedEnvironmentKey = EnvironmentKey.string()
            .defaulted("DEPLOYED_ENVIRONMENT", LOCAL.name)

        fun Environment.deployedEnvironment(): DeployedEnvironment =
            valueOf(deployedEnvironmentKey(this))
    }
}

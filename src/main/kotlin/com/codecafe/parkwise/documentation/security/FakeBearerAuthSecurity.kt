package com.codecafe.parkwise.documentation.security

import org.http4k.contract.openapi.Render
import org.http4k.contract.openapi.RenderModes
import org.http4k.contract.openapi.rendererFor
import org.http4k.core.Filter
import org.http4k.security.Security

object FakeBearerAuth {
    operator fun invoke(token: String): Filter = FakeBearerAuth { it == token }

    operator fun invoke(@Suppress("UNUSED_PARAMETER") checkToken: (String) -> Boolean): Filter =
        Filter { next -> { next(it) } }
}

class FakeBearerAuthSecurity private constructor(
    override val filter: Filter,
    val name: String = "bearerAuth"
) : Security {

    constructor(token: String, name: String = "bearerAuth") : this(FakeBearerAuth(token), name)

    companion object {
        val renderer
            get() = rendererFor<FakeBearerAuthSecurity> {
                object : RenderModes {
                    override fun <NODE> full(): Render<NODE> = {
                        obj(
                            it.name to obj(
                                "scheme" to string("bearer"),
                                "type" to string("http")
                            )
                        )
                    }

                    override fun <NODE> ref(): Render<NODE> = {
                        obj(it.name to array(emptyList()))
                    }
                }
            }
    }
}

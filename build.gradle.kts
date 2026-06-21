import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.detekt)

    application
}

group = "com.codecafe.parkwise"
version = "0.1.0"

detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom("$rootDir/detekt.yml")

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:${libs.versions.detekt.get()}")
    }
}

kotlin {
    sourceSets["test"].kotlin.srcDir("src/test/kotlin")
}

dependencies {
    implementation(platform(libs.kotlin.bom))
    implementation(platform(libs.http4k.bom))

    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.slf4j)

    implementation(libs.http4k.core)
    implementation(libs.http4k.format.jackson)
    implementation(libs.http4k.server.jetty)
    implementation(libs.http4k.client.okhttp)
    implementation(libs.http4k.platform.core)
    implementation(libs.http4k.platform.k8s)
    implementation(libs.http4k.config)
    implementation(libs.http4k.api.openapi)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.datatype.jsr310)

    implementation(libs.slf4j.api)
    implementation(libs.kotlin.logging)
    implementation(libs.logback.classic)
    implementation(libs.logstash.logback.encoder)

    implementation(libs.java.jwt)
    implementation(libs.caffeine)

    implementation(libs.problem)
    implementation(libs.problem.jackson)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.assertions.json)
    testImplementation(libs.kotest.property.jvm)
    testImplementation(libs.kotest.assertions.arrow)
    testImplementation(libs.mockk)
    testImplementation(libs.http4k.testing.kotest)
    testImplementation(libs.wiremock.standalone)
    testImplementation(libs.kotest.testcontainers)
    testImplementation(libs.commons.compress)
    testImplementation(libs.datafaker)
}

application {
    mainClass.set("com.codecafe.parkwise.ApplicationKt")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
        jvmArgs("--add-opens=java.base/java.util=ALL-UNNAMED")
        systemProperties = System.getProperties()
            .map { it.key.toString() to it.value }
            .toMap()
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
        compilerOptions.freeCompilerArgs.addAll(
            listOf(
                "-Xjsr305=strict",
                "-opt-in=kotlin.contracts.ExperimentalContracts,kotlin.RequiresOptIn"
            )
        )
    }

    named("check") {
        dependsOn("test")
    }
}
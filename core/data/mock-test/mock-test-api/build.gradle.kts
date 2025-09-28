plugins {
    id("kotlin")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    api(projects.core.model)
    api(libs.kotlinx.datetime)
    api(projects.core.data.test.testApi)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
}

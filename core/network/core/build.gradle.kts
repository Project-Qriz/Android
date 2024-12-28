plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.qriz.app.core.network.core"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}
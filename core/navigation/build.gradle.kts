plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.qriz.app.core.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}

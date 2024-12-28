plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.qriz.app.core.network.onboard"
}

dependencies {
    api(projects.core.network.common)
    implementation(projects.core.data.onboard.onboardApi)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}
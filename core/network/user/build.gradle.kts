plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    namespace = "com.qriz.app.core.network.user"
}

dependencies {
    api(projects.core.network.common)
    implementation(projects.core.data.user.userApi)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}
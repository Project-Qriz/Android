import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    setNamespace("core.network.user")
}

dependencies {
    api(projects.core.network.common)
    implementation(projects.core.data.user.userApi)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}
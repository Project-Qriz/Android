import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    setNamespace("core.network.onboard")
}

dependencies {
    api(projects.core.network.common)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

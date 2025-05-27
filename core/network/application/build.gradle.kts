import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.application")
}

dependencies {
    api(projects.core.network.common)
    api(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

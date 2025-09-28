import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.mock_test")
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

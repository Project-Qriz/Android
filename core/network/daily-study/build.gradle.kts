import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.daily_study")
}

dependencies {
    api(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

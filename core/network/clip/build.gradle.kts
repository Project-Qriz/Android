import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.clip")
}

dependencies {
    api(projects.core.model)
    api(projects.core.network.common)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

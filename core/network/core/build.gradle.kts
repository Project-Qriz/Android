import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.network.core")
}

dependencies {
    implementation(projects.core.data.token.tokenApi)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.retrofit)
}

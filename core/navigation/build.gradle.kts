import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.navigation")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}

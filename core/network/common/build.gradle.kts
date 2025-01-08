import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}
android {
    setNamespace("core.network.common")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}

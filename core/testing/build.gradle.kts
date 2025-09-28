import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.testing")
}

dependencies {
    implementation(libs.bundles.unit.test)
    implementation(libs.androidx.compose.navigation)
}

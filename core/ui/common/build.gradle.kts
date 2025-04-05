import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
    id("qriz.android.compose")
}

android {
    setNamespace("core.ui.common")
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(libs.coil.core)
}

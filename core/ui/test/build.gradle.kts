import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    id("qriz.android.compose")
}

android {
    setNamespace("core.ui.test")
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.data.test.testApi)
}

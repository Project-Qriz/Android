import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.application.application")
}

dependencies {
    implementation(projects.core.data.application.applicationApi)
}

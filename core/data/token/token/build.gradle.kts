import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.token.token")
}

dependencies {
    implementation(projects.core.data.token.tokenApi)
    implementation(projects.core.datastore)
}

import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.user.user")
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.network.user)
    implementation(projects.core.datastore)
}
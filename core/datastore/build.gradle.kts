import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.data.user.userApi)
}

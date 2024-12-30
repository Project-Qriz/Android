import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.datastore")
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
}

import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("feature.base")
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
}

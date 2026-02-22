import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.clip.clip")
}

dependencies {
    implementation(projects.core.data.clip.clipApi)
    implementation(projects.core.network.clip)
}

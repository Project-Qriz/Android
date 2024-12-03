plugins {
    id("qriz.android.feature")
}

android {
    namespace = "com.qriz.app.feature.main"
}

dependencies {
    implementation(projects.feature.sign)
}

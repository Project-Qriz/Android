plugins {
    id("qriz.android.feature")
}

android {
    namespace = "com.qriz.app.feature.onboard"
}

dependencies {
    implementation(projects.core.data.onboard.onboardApi)
}
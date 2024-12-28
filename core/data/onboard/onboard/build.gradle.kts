plugins {
    id("qriz.android.library")
}

android {
    namespace = "com.qriz.app.core.data.onboard.onboard"
}

dependencies {
    implementation(projects.core.data.onboard.onboardApi)
    implementation(projects.core.network.onboard)
    implementation(projects.core.datastore)
}
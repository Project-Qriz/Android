plugins {
    id("qriz.android.application")
}

android {
    namespace = "com.qriz.app"

    defaultConfig {
        applicationId = "com.qriz.app"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(projects.core.network.core)
    implementation(projects.core.data.user.user)
    implementation(projects.core.data.onboard.onboard)
    implementation(projects.core.data.token.token)
    implementation(projects.core.data.conceptbook.conceptbook)
    implementation(projects.feature.main)
    implementation(projects.feature.splash)
}

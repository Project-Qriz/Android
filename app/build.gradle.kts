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

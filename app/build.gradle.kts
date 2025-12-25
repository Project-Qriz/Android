plugins {
    id("qriz.android.application")
}

android {
    namespace = "com.qriz.app"

    defaultConfig {
        applicationId = "com.qriz.app"
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

dependencies {
    implementation(projects.core.network.core)

    implementation(projects.core.domain.usecase)

    implementation(projects.core.data.user.user)
    implementation(projects.core.data.onboard.onboard)
    implementation(projects.core.data.token.token)
    implementation(projects.core.data.conceptbook.conceptbook)
    implementation(projects.core.data.application.application)
    implementation(projects.core.data.dailyStudy.dailyStudy)
    implementation(projects.core.data.mockTest.mockTest)

    implementation(projects.feature.main)
    implementation(projects.feature.splash)

    implementation(projects.core.ui.common)

    implementation(libs.coil.compose)
    implementation(libs.coil.core)
}

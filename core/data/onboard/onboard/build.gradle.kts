import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("data.onboard.onboard")
}

dependencies {
    implementation(projects.core.data.onboard.onboardApi)
    implementation(projects.core.network.onboard)
    implementation(projects.core.datastore)
}
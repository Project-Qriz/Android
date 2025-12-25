import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.onboard")
}

dependencies {
    implementation(projects.core.data.onboard.onboardApi)
    implementation(projects.core.domain.usecaseApi)
    implementation(projects.core.ui.test)
    implementation(projects.core.data.user.userApi)
}

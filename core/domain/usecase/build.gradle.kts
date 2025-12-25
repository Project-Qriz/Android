plugins {
    id("qriz.android.library")
}

android {
    namespace = "com.qriz.app.core.domain.usecase"
}

dependencies {
    implementation(projects.core.domain.usecaseApi)

    implementation(projects.core.data.onboard.onboardApi)
    implementation(projects.core.data.user.userApi)
}

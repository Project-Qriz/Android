plugins {
    id("qriz.android.feature")
}

android {
    namespace = "com.qriz.app.feature.splash"
}

dependencies {
    implementation(projects.feature.main)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}

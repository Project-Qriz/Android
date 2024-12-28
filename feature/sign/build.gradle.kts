plugins {
    id("qriz.android.feature")
}

android {
    namespace = "com.qriz.app.feature.sign"
}

dependencies {
    implementation(projects.core.data.user.userApi)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

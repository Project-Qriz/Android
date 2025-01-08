import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.splash")
}

dependencies {
    implementation(projects.core.data.user.userApi)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}

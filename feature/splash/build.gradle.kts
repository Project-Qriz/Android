import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.splash")
}

dependencies {
    implementation(projects.feature.main) //TODO : 네비게이션으로 변경 후 제거
    implementation(projects.core.data.user.userApi)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}

import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.mock_test")
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.data.mockTest.mockTestApi)
    implementation(projects.core.ui.test)
    implementation(libs.vico.compose)
}

import com.qriz.app.getLocalProperty
import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.sign")

    defaultConfig {
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            getLocalProperty("KAKAO_NATIVE_APP_KEY_PROPERTY")
        )
        buildConfigField(
            "String",
            "GOOGLE_CLOUD_CONSOLE_CLIENT_ID",
            getLocalProperty("GOOGLE_CLOUD_CONSOLE_CLIENT_ID")
        )
    }
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.ui.common)

    implementation(libs.kakao.sdk.user)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.google.android.libraries.identity.googleid)
}

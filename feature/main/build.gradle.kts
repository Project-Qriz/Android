import com.qriz.app.getLocalProperty
import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

val kakaoNativeAppKey = "KAKAO_NATIVE_APP_KEY"

android {
    setNamespace("feature.main")

    buildTypes {
        debug {
            manifestPlaceholders[kakaoNativeAppKey] = getLocalProperty(kakaoNativeAppKey)
        }
        release {
            isMinifyEnabled = true
            manifestPlaceholders[kakaoNativeAppKey] = getLocalProperty(kakaoNativeAppKey)
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)

    implementation(projects.feature.splash)
    implementation(projects.feature.sign)
    implementation(projects.feature.onboard)
    implementation(projects.feature.home)
    implementation(projects.feature.conceptBook)
    implementation(projects.feature.incorrectAnswersNote)
    implementation(projects.feature.mypage)
    implementation(projects.feature.dailyStudy)
    implementation(projects.feature.mockTest)
}

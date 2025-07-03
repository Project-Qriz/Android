import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.main")
}

dependencies {
    implementation(projects.feature.splash)
    implementation(projects.feature.sign)
    implementation(projects.feature.onboard)
    implementation(projects.feature.home)
    implementation(projects.feature.conceptBook)
    implementation(projects.feature.incorrectAnswersNote)
    implementation(projects.feature.mypage)
    implementation(projects.feature.dailyStudy)
}

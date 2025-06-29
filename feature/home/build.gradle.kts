import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.home")
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.data.application.applicationApi)
    implementation(projects.core.data.dailyStudy.dailyStudyApi)
}

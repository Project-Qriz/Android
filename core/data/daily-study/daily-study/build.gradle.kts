import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library.testable")
}

android {
    setNamespace("core.data.daily_study.daily_study")
}

dependencies {
    implementation(projects.core.data.dailyStudy.dailyStudyApi)
    implementation(projects.core.network.dailyStudy)
}

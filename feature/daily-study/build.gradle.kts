import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("featrue.daily_study")
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.data.dailyStudy.dailyStudyApi)
    implementation(projects.core.data.conceptbook.conceptbookApi)
    implementation(projects.core.ui.test)
}

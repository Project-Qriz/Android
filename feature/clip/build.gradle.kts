import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.clip")
}

dependencies {
    implementation(projects.core.data.clip.clipApi)
    implementation(projects.core.data.conceptbook.conceptbookApi)
    implementation(projects.core.domain.usecaseApi)
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.ui.test)
}

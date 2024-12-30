import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.sign")
}

dependencies {
    implementation(projects.core.data.user.userApi)
}

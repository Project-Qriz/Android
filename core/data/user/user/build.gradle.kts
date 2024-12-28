plugins {
    id("qriz.android.library")
}

android {
    namespace = "com.qriz.app.core.data.user.user"
}

dependencies {
    implementation(projects.core.data.user.userApi)
    implementation(projects.core.network.user)
    implementation(projects.core.datastore)
}
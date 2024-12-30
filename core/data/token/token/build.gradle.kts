plugins {
    id("qriz.android.library")
}

android {
    namespace = "com.qriz.core.data.token.token"
}

dependencies {
    implementation(projects.core.data.token.tokenApi)
    implementation(projects.core.datastore)
}

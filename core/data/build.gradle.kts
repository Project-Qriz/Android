plugins {
    id("qriz.android.library")
}

android {
    namespace = "com.qriz.app.core.data"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.datastore)
}

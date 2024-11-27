import com.qriz.app.findLibrary

plugins {
    id("qriz.android.library")
    id("qriz.android.compose")
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))

    implementation(findLibrary("androidx.core.ktx"))
    implementation(findLibrary("androidx.appcompat"))
    implementation(findLibrary("material"))
    implementation(findLibrary("hilt.navigation.compose"))
}

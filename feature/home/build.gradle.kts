import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.home")
}

dependencies {
    implementation(project(":core:data:user:user-api"))
}

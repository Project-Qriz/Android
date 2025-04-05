import com.qriz.app.setNamespace

plugins {
    id("qriz.android.feature")
}

android {
    setNamespace("feature.concept_book")
}

dependencies {
    implementation(projects.core.data.conceptbook.conceptbookApi)

    implementation(libs.coil.compose)
}

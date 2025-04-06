import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.conceptbook.conceptbook")
}

dependencies {
    implementation(projects.core.data.conceptbook.conceptbookApi)
}

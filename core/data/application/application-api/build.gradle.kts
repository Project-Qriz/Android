import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.conceptbook.application_api")
}

dependencies {
    api(projects.core.model)
}

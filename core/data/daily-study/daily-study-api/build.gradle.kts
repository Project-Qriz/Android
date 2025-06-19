import com.qriz.app.setNamespace

plugins {
    id("qriz.android.library")
}

android {
    setNamespace("core.data.conceptbook.daily_study_api")
}

dependencies {
    api(projects.core.model)
}

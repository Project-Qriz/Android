plugins {
	id("qriz.android.library")
}
android {
	namespace = "com.qriz.app.core.testing"
}

dependencies {
	api(libs.bundles.unit.test)
}

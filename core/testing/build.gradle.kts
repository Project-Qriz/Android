import com.qriz.app.setNamespace

plugins {
	id("qriz.android.library")
}
android {
	setNamespace("core.testing")
}

dependencies {
	api(libs.bundles.unit.test)
}

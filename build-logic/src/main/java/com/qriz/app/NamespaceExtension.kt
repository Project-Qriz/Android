package com.qriz.app

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.qriz.app.$name"
    }
}
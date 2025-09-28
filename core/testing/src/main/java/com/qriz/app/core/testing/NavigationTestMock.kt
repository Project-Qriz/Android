package com.qriz.app.core.testing

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import io.mockk.every
import io.mockk.mockkStatic

inline fun <reified T : Any> SavedStateHandle.mockkToRoute(page: T) {
    mockkStatic("androidx.navigation.SavedStateHandleKt")
    every { toRoute<T>() } returns page
}

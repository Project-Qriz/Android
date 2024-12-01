package com.qriz.app.core.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val flowLogin: Flow<Boolean>
}

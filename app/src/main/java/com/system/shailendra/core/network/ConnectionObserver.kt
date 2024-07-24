package com.system.shailendra.core.network

import kotlinx.coroutines.flow.Flow

interface ConnectionObserver {
    fun observe(): Flow<Status>
}
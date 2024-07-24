package com.system.shailendra.dashboard.presentation

import com.system.shailendra.core.network.Status

sealed class DashboardScreenEvent {
    data object CheckDataAndPost: DashboardScreenEvent()
    data object ClearNotificationState: DashboardScreenEvent()
    data object RetryLogin: DashboardScreenEvent()
    data class CheckToken(val connectionStatus: Status): DashboardScreenEvent()
}
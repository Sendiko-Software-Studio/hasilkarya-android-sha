package com.system.hasilkarya.dashboard.presentation

sealed class DashboardScreenEvent {
    data object CheckDataAndPost: DashboardScreenEvent()
    data object ClearNotificationState: DashboardScreenEvent()
    data class SetStationSheet(val isVisible: Boolean): DashboardScreenEvent()
}
package com.iav.contestclient.ui.state

// UI state sealed class
sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
    class Sumit: UiState()
}



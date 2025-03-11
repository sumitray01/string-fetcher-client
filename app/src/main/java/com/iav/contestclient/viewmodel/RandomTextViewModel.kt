package com.iav.contestclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iav.contestclient.data.model.RandomStringEntity
import com.iav.contestclient.repository.RandomTextRepository
import com.iav.contestclient.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomTextViewModel @Inject constructor(
    private val repository: RandomTextRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Collect all random strings from the database as StateFlow
    val randomStrings: StateFlow<List<RandomStringEntity>> =
        repository.allRandomStrings.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun generateRandomString(desiredLength: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.fetchAndStoreRandomString(desiredLength)
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun deleteRandomString(entity: RandomStringEntity) {
        viewModelScope.launch {
            repository.deleteRandomString(entity)
        }
    }

    fun deleteAllRandomStrings() {
        viewModelScope.launch {
            repository.deleteAllRandomStrings()
        }
    }
}

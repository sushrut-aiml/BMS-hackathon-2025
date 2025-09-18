
package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.PredictionResponse
import com.example.myapplication.data.repository.PredictionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class PredictionUiState {
    object Idle : PredictionUiState()
    object Loading : PredictionUiState()
    data class Success(val result: PredictionResponse) : PredictionUiState()
    data class Error(val message: String) : PredictionUiState()
}

class PredictionViewModel : ViewModel() {

    private val repository = PredictionRepository()

    private val _uiState = MutableStateFlow<PredictionUiState>(PredictionUiState.Idle)
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()

    fun getPrediction(symptoms: String) {
        viewModelScope.launch {
            _uiState.value = PredictionUiState.Loading

            repository.getPrediction(symptoms)
                .onSuccess { result ->
                    _uiState.value = PredictionUiState.Success(result)
                }
                .onFailure { exception ->
                    _uiState.value = PredictionUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    fun clearState() {
        _uiState.value = PredictionUiState.Idle
    }
}
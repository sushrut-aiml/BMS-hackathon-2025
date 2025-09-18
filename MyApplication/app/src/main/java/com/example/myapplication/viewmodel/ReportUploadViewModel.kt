package com.example.myapplication.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.ReceiptAnalysisResponse
import com.example.myapplication.data.repository.ReceiptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Sealed class for UI states

class ReportUploadViewModel : ViewModel() {

    private val repository = ReceiptRepository()

    // UI State management
    private val _uiState = MutableStateFlow<UploadUiState>(UploadUiState.Idle)
    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    // Selected file name
    private val _selectedFileName = MutableStateFlow<String?>(null)
    val selectedFileName: StateFlow<String?> = _selectedFileName.asStateFlow()

    /**
     * Analyze the selected receipt using AI
     */
    fun analyzeReceipt(context: Context, uri: Uri, fileName: String) {
        viewModelScope.launch {
            _uiState.value = UploadUiState.Loading

            repository.analyzeReceipt(context, uri, fileName)
                .onSuccess { result ->
                    _uiState.value = UploadUiState.Success(result)
                }
                .onFailure { exception ->
                    _uiState.value = UploadUiState.Error(
                        exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }

    /**
     * Set the selected file name
     */
    fun setSelectedFile(fileName: String) {
        _selectedFileName.value = fileName
    }

    /**
     * Clear the current state and reset to idle
     */
    fun clearState() {
        _uiState.value = UploadUiState.Idle
        _selectedFileName.value = null
    }

    /**
     * Reset only the upload state (keep file selected)
     */
    fun resetUploadState() {
        _uiState.value = UploadUiState.Idle
    }
}
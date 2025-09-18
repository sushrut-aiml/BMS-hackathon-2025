// ============================================================
// FILE: UploadUiState.kt
// Location: app/src/main/java/com/example/myapplication/viewmodel/UploadUiState.kt
// ============================================================

package com.example.myapplication.viewmodel

import com.example.myapplication.data.model.ReceiptAnalysisResponse

/**
 * Sealed class representing different UI states for file upload and analysis
 */
sealed class UploadUiState {
    /**
     * Initial state - no file selected, no operation in progress
     */
    object Idle : UploadUiState()

    /**
     * Loading state - file is being uploaded and analyzed
     */
    object Loading : UploadUiState()

    /**
     * Success state - analysis completed successfully
     * @param result The analysis response from the API
     */
    data class Success(val result: ReceiptAnalysisResponse) : UploadUiState()

    /**
     * Error state - something went wrong
     * @param message Error message to display to user
     */
    data class Error(val message: String) : UploadUiState()
}
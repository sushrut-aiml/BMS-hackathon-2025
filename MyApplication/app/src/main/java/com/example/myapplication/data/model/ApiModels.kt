package com.example.myapplication.data.model

data class ReceiptAnalysisRequest(
    val file: String, // Base64 encoded file
    val fileType: String, // "pdf", "jpg", "png", etc.
    val fileName: String
)

data class ReceiptAnalysisResponse(
    val success: Boolean,
    val message: String,
    val simplifiedText: String? = null,
    val keyInformation: KeyInfo? = null,
    val error: String? = null
)

data class KeyInfo(
    val doctorName: String? = null,
    val patientName: String? = null,
    val date: String? = null,
    val medicines: List<Medicine>? = null,
    val totalAmount: Double? = null,
    val diagnosis: String? = null,
    val clinic: String? = null
)

data class Medicine(
    val name: String,
    val dosage: String,
    val frequency: String,
    val duration: String? = null
)

data class PredictionRequest(
    val symptoms: String,
    val apiKey: String
)

data class PredictionResponse(
    val success: Boolean,
    val prediction: String?,
    val confidence: Double?,
    val suggestions: List<String>?,
    val error: String?
)
package com.example.myapplication.data.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ReceiptRepository {

    // Toggle this for testing without real API
    private val MOCK_MODE = true // Set to false when your friend's API is ready

    suspend fun analyzeReceipt(
        context: Context,
        uri: Uri,
        fileName: String
    ): Result<ReceiptAnalysisResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (MOCK_MODE) {
                    // Mock response for testing
                    delay(2000) // Simulate API call delay

                    val mockResponse = ReceiptAnalysisResponse(
                        success = true,
                        message = "Analysis completed successfully (MOCK DATA)",
                        simplifiedText = "This is a medical prescription from Dr. Smith for fever treatment. The patient John Doe was prescribed Paracetamol and Cough Syrup for 5 days.",
                        keyInformation = KeyInfo(
                            doctorName = "Dr. Rajesh Smith",
                            patientName = "John Doe",
                            date = "2024-01-15",
                            clinic = "City Medical Center",
                            diagnosis = "Viral Fever, Cough",
                            totalAmount = 450.0,
                            medicines = listOf(
                                Medicine(
                                    name = "Paracetamol 500mg",
                                    dosage = "500mg",
                                    frequency = "2 times daily",
                                    duration = "5 days"
                                ),
                                Medicine(
                                    name = "Cough Syrup",
                                    dosage = "10ml",
                                    frequency = "3 times daily",
                                    duration = "5 days"
                                ),
                                Medicine(
                                    name = "Vitamin C",
                                    dosage = "1 tablet",
                                    frequency = "1 time daily",
                                    duration = "7 days"
                                )
                            )
                        )
                    )

                    Result.success(mockResponse)
                } else {
                    // Real API call (when your friend's API is ready)
                    performRealApiCall(context, uri, fileName)
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private suspend fun performRealApiCall(
        context: Context,
        uri: Uri,
        fileName: String
    ): Result<ReceiptAnalysisResponse> {
        return try {
            // Convert URI to file
            val file = uriToFile(context, uri, fileName)
            val fileExtension = file.extension.lowercase()

            // Create multipart request
            val requestFile = file.asRequestBody("*/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val fileType = fileExtension.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = ApiService.receiptApi.analyzeReceiptMultipart(body, fileType)

            // Clean up temporary file
            file.delete()

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }

        } catch (e: Exception) {
            Result.failure(Exception("Network Error: ${e.message}"))
        }
    }

    private fun uriToFile(context: Context, uri: Uri, fileName: String): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return file
    }
}
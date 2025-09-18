package com.example.myapplication.data.api

import com.example.myapplication.data.model.ReceiptAnalysisRequest
import com.example.myapplication.data.model.ReceiptAnalysisResponse
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ReceiptAnalysisApi {

    @Multipart
    @POST("analyze-receipt")
    suspend fun analyzeReceiptMultipart(
        @Part file: MultipartBody.Part,
        @Part("fileType") fileType: RequestBody
    ): Response<ReceiptAnalysisResponse>

    @POST("analyze-receipt")
    suspend fun analyzeReceiptBase64(
        @Body request: ReceiptAnalysisRequest
    ): Response<ReceiptAnalysisResponse>

    @GET("health")
    suspend fun checkHealth(): Response<Map<String, String>>
}
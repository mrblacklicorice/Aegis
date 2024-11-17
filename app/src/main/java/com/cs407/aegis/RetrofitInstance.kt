package com.cs407.aegis

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.openai.com/"

    // Create an OkHttpClient with logging and an interceptor to add the API key to headers
    private val okHttpClient = OkHttpClient.Builder().apply {
        // Adding logging interceptor for debug purposes
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        addInterceptor(loggingInterceptor)

        // Add an interceptor to inject the API key dynamically into the request headers
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")  // Access the API key from BuildConfig
                .build()
            chain.proceed(request)
        }
    }.build()

    // Build the Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create and expose the OpenAI API service
    val api: OpenAIService = retrofit.create(OpenAIService::class.java)
}
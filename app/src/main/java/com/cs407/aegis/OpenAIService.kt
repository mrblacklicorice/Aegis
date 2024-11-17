package com.cs407.aegis

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data classes for handling request and response
data class ChatRequest(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int = 1000,
)

data class Message(
    val role: String,
    val content: String
)

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)

// This is the interface defining the API call
interface OpenAIService {
    @POST("v1/chat/completions")
    fun getChatResponse(@Body request: ChatRequest): Call<ChatResponse>
}
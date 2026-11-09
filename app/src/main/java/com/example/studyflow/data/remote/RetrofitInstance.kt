package com.example.studyflow.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Base URL must end with "/". The path "api/tasks.json" comes from TaskApi.
    private const val BASE_URL = "https://raw.githubusercontent.com/inarchik005/StudyFlow/main/"

    // Created once, the first time someone uses it.
    val api: TaskApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // JSON -> TaskDto
            .build()
            .create(TaskApi::class.java)
    }
}

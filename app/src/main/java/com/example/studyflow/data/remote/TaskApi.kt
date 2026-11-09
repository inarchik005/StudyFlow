package com.example.studyflow.data.remote

import retrofit2.http.GET

// Retrofit turns this interface into a real HTTP call:
// GET https://raw.githubusercontent.com/inarchik005/StudyFlow/main/api/tasks.json
interface TaskApi {
    @GET("api/tasks.json")
    suspend fun getTasks(): List<TaskDto>
}

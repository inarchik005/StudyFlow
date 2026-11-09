package com.example.studyflow.data.remote

import com.google.gson.annotations.SerializedName

// Exactly the shape of one object in api/tasks.json.
// Fields are nullable because a server can always send a missing value.
data class TaskDto(
    val id: Int,
    val title: String?,
    val subject: String?,
    @SerializedName("due_date") val dueDate: String?,
    val description: String?,
    val priority: String?
)

package com.example.studyflow.domain.model

// The app's own model. Screens and the ViewModel only work with this class,
// never with the API class (TaskDto).
data class StudyTask(
    val id: Int,
    val title: String,
    val subject: String,
    val deadline: String,
    val description: String,
    val priority: String
)

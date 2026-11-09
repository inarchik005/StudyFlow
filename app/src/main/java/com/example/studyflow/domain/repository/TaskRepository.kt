package com.example.studyflow.domain.repository

import com.example.studyflow.domain.model.StudyTask

// The contract: "give me the tasks". It does not say where they come from.
// No Retrofit and no Android code here.
interface TaskRepository {
    suspend fun getTasks(): List<StudyTask>
}

package com.example.studyflow.data.repository

import com.example.studyflow.data.remote.TaskApi
import com.example.studyflow.data.remote.TaskDto
import com.example.studyflow.domain.model.StudyTask
import com.example.studyflow.domain.repository.TaskRepository

// Implements the domain contract using the network.
// 1) calls the API  2) turns every TaskDto into a StudyTask.
class TaskRepositoryImpl(
    private val api: TaskApi
) : TaskRepository {

    override suspend fun getTasks(): List<StudyTask> {
        return api.getTasks().map { dto -> dto.toStudyTask() }
    }

    private fun TaskDto.toStudyTask(): StudyTask {
        return StudyTask(
            id = id,
            title = title ?: "Untitled task",
            subject = subject ?: "Other",
            deadline = dueDate ?: "No deadline",
            description = description ?: "",
            priority = priority ?: "Medium"
        )
    }
}

package com.example.studyflow.presentation

import com.example.studyflow.domain.model.StudyTask

// What happened with the network request.
sealed interface TasksState {
    data object Loading : TasksState
    data class Success(val tasks: List<StudyTask>) : TasksState
    data class Error(val message: String) : TasksState
}

data class HomeUiState(
    val tasksState: TasksState = TasksState.Loading,
    // Tasks after search + category filter, prepared by the ViewModel.
    val visibleTasks: List<StudyTask> = emptyList(),
    val categories: List<String> = listOf("All", "Math", "Programming", "Android", "University", "AI", "Languages", "Cybersecurity"),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val favoriteTaskIds: Set<Int> = emptySet()
)

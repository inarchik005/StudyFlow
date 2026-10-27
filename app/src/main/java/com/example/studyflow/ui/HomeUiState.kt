package com.example.studyflow.ui

import com.example.studyflow.data.StudyTask

data class HomeUiState(
    // tasks is the complete repository result; visibleTasks is prepared by the ViewModel.
    val tasks: List<StudyTask> = emptyList(),
    val visibleTasks: List<StudyTask> = emptyList(),
    val categories: List<String> = listOf("All", "Math", "Programming", "Android", "University", "AI", "Languages", "Cybersecurity"),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val favoriteTaskIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

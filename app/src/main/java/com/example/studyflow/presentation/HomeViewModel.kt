package com.example.studyflow.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studyflow.data.remote.RetrofitInstance
import com.example.studyflow.data.repository.TaskRepositoryImpl
import com.example.studyflow.domain.model.StudyTask
import com.example.studyflow.domain.repository.TaskRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    // The ViewModel only knows the interface from domain, not Retrofit.
    private val repository: TaskRepository = TaskRepositoryImpl(RetrofitInstance.api)
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        Log.d("HomeViewModel", "init")
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.update { it.copy(tasksState = TasksState.Loading) }
            try {
                val tasks = repository.getTasks()
                _uiState.update {
                    it.copy(tasksState = TasksState.Success(tasks)).withVisibleTasks()
                }
            } catch (e: CancellationException) {
                throw e // the coroutine was cancelled, this is not a real error
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to load tasks", e)
                val message = if (e is IOException) {
                    "No internet connection. Check your Wi-Fi and try again."
                } else {
                    "Could not load tasks: ${e.message}"
                }
                _uiState.update { it.copy(tasksState = TasksState.Error(message)) }
            }
        }
    }

    fun onRetry() {
        loadTasks()
    }

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(searchQuery = query).withVisibleTasks() }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category).withVisibleTasks() }
    }

    fun onFavouriteClick(taskId: Int) {
        _uiState.update { state ->
            val favorites = state.favoriteTaskIds.toMutableSet()
            if (taskId in favorites) favorites.remove(taskId) else favorites.add(taskId)
            state.copy(favoriteTaskIds = favorites)
        }
    }

    fun getTask(taskId: Int): StudyTask? {
        val state = _uiState.value.tasksState
        return (state as? TasksState.Success)?.tasks?.firstOrNull { it.id == taskId }
    }

    override fun onCleared() {
        Log.d("HomeViewModel", "onCleared")
        super.onCleared()
    }

    private fun HomeUiState.withVisibleTasks(): HomeUiState {
        val allTasks = (tasksState as? TasksState.Success)?.tasks ?: emptyList()
        val query = searchQuery.trim()
        return copy(
            visibleTasks = allTasks.filter { task ->
                (selectedCategory == "All" || task.subject == selectedCategory) &&
                    (query.isEmpty() || task.title.contains(query, ignoreCase = true) ||
                        task.subject.contains(query, ignoreCase = true))
            }
        )
    }
}

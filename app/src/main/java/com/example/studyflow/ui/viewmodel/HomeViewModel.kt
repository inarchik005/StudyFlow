
package com.example.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import com.example.studyflow.data.TaskRepository
import com.example.studyflow.ui.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        android.util.Log.d("HomeViewModel", "init")
        loadTasks()
    }

    fun loadTasks(shouldFail: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                val tasks = repository.getTasks(shouldFail)
                _uiState.update {
                    it.copy(
                        tasks = tasks,
                        isLoading = false,
                        error = null
                    ).withVisibleTasks()
                }
            } catch (exception: Exception) {
                if (exception is CancellationException) throw exception
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.update { state ->
            state.copy(searchQuery = query).withVisibleTasks()
        }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { state ->
            state.copy(selectedCategory = category).withVisibleTasks()
        }
    }

    fun onFavouriteClick(taskId: Int) {
        _uiState.update { state ->
            val favorites = state.favoriteTaskIds.toMutableSet()
            if (taskId in favorites) {
                favorites.remove(taskId)
            } else {
                favorites.add(taskId)
            }
            state.copy(favoriteTaskIds = favorites).withVisibleTasks()
        }
    }

    fun onRetry() {
        loadTasks()
    }

    fun onSimulateError() {
        loadTasks(shouldFail = true)
    }

    override fun onCleared() {
        android.util.Log.d("HomeViewModel", "onCleared")
        super.onCleared()
    }

    private fun HomeUiState.withVisibleTasks(): HomeUiState {
        val query = searchQuery.trim()
        return copy(
            visibleTasks = tasks.filter { task ->
                (selectedCategory == "All" || task.subject == selectedCategory) &&
                    (query.isEmpty() || task.title.contains(query, ignoreCase = true) ||
                        task.subject.contains(query, ignoreCase = true))
            }
        )
    }
}

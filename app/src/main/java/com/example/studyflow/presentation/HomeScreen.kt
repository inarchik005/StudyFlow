package com.example.studyflow.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyflow.CategoryChip
import com.example.studyflow.SectionHeader
import com.example.studyflow.Spacing
import com.example.studyflow.TaskCard
import com.example.studyflow.domain.model.StudyTask
import com.example.studyflow.ui.theme.StudyFlowTheme

// Connects the ViewModel to the stateless screen.
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    onTaskClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onSearchChange = viewModel::onSearchChange,
        onCategorySelected = viewModel::onCategorySelected,
        onFavouriteClick = viewModel::onFavouriteClick,
        onRetry = viewModel::onRetry,
        onTaskClick = onTaskClick,
        onProfileClick = onProfileClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onFavouriteClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "StudyFlow", style = MaterialTheme.typography.headlineSmall)
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Text(text = "👤", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = Spacing.Medium)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                label = { Text("Search tasks") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            SectionHeader(title = "Categories")

            LazyRow(horizontalArrangement = Arrangement.spacedBy(Spacing.Small)) {
                items(uiState.categories) { category ->
                    CategoryChip(
                        category = category,
                        selected = uiState.selectedCategory == category,
                        onClick = { onCategorySelected(category) }
                    )
                }
            }

            SectionHeader(title = "Your tasks")

            // One branch for every network state.
            when (val state = uiState.tasksState) {
                TasksState.Loading -> LoadingState()
                is TasksState.Error -> ErrorState(message = state.message, onRetry = onRetry)
                is TasksState.Success -> {
                    if (uiState.visibleTasks.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(Spacing.Small)) {
                            items(items = uiState.visibleTasks, key = { it.id }) { task ->
                                TaskCard(
                                    task = task,
                                    onClick = { onTaskClick(task.id) },
                                    isFavorite = task.id in uiState.favoriteTaskIds,
                                    onFavoriteClick = { onFavouriteClick(task.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Large),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Large),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No tasks found. You're all caught up!",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// ---------- Previews: fake states, no network needed ----------

val previewTasks = listOf(
    StudyTask(1, "Complete Android Development homework", "Android", "Nov 10",
        "Finish the Jetpack Compose screen and prepare the project for submission.", "High"),
    StudyTask(2, "Review Kotlin basics", "Programming", "Nov 11",
        "Review variables, functions, collections and null safety in Kotlin.", "Medium"),
    StudyTask(3, "Prepare Deep Learning assignment", "AI", "Nov 12",
        "Complete the required exercises and check the final notebook.", "High")
)

@Composable
private fun HomePreview(uiState: HomeUiState) {
    StudyFlowTheme {
        HomeScreen(
            uiState = uiState,
            onSearchChange = {}, onCategorySelected = {}, onFavouriteClick = {},
            onRetry = {}, onTaskClick = {}, onProfileClick = {}
        )
    }
}

@Preview(name = "Success", showBackground = true)
@Composable
fun HomeSuccessPreview() {
    HomePreview(HomeUiState(tasksState = TasksState.Success(previewTasks), visibleTasks = previewTasks))
}

@Preview(name = "Success dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeSuccessDarkPreview() {
    HomePreview(HomeUiState(tasksState = TasksState.Success(previewTasks), visibleTasks = previewTasks))
}

@Preview(name = "Loading", showBackground = true)
@Composable
fun HomeLoadingPreview() {
    HomePreview(HomeUiState(tasksState = TasksState.Loading))
}

@Preview(name = "Empty", showBackground = true)
@Composable
fun HomeEmptyPreview() {
    HomePreview(HomeUiState(tasksState = TasksState.Success(emptyList())))
}

@Preview(name = "Error", showBackground = true)
@Composable
fun HomeErrorPreview() {
    HomePreview(HomeUiState(tasksState = TasksState.Error("No internet connection. Check your Wi-Fi and try again.")))
}

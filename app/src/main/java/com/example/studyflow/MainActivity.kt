package com.example.studyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyflow.presentation.HomeViewModel
import com.example.studyflow.ui.theme.StudyFlowTheme
import com.example.studyflow.domain.model.StudyTask
import com.example.studyflow.presentation.HomeRoute
import com.example.studyflow.presentation.previewTasks

object Spacing {
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("MainActivity", "onCreate")

        enableEdgeToEdge()

        setContent {
            StudyFlowTheme {
                StudyFlowApp()
            }
        }
    }

    override fun onStart() { super.onStart(); android.util.Log.d("MainActivity", "onStart") }
    override fun onResume() { super.onResume(); android.util.Log.d("MainActivity", "onResume") }
    override fun onPause() { android.util.Log.d("MainActivity", "onPause"); super.onPause() }
    override fun onStop() { android.util.Log.d("MainActivity", "onStop"); super.onStop() }
    override fun onDestroy() { android.util.Log.d("MainActivity", "onDestroy"); super.onDestroy() }
}

@Composable
fun StudyFlowApp() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeRoute(
                viewModel = homeViewModel,
                onTaskClick = { taskId ->
                    navController.navigate("details/$taskId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        composable(
            route = "details/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 1
            val task = homeViewModel.getTask(taskId)

            if (task != null) {
                DetailScreen(
                    task = task,
                    isFavorite = task.id in uiState.favoriteTaskIds,
                    onFavoriteClick = { homeViewModel.onFavouriteClick(task.id) },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("profile") {
            ProfileScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    task: StudyTask,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Task Details"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Medium
            )
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.study_flow_icon
                ),
                contentDescription = "StudyFlow study icon",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            InfoRow(
                label = "Subject",
                value = task.subject
            )

            InfoRow(
                label = "Deadline",
                value = task.deadline
            )

            InfoRow(
                label = "Priority",
                value = task.priority
            )

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = onFavoriteClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isFavorite) {
                        "★ Added to favorites"
                    } else {
                        "☆ Add to favorites"
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Profile")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Text(
                            text = "←",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(Spacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                Spacing.Medium
            )
        ) {

            Image(
                painter = painterResource(
                    id = R.drawable.study_flow_icon
                ),
                contentDescription = "StudyFlow profile icon",
                modifier = Modifier.size(110.dp)
            )

            Text(
                text = "Inara",
                style = MaterialTheme.typography.headlineMedium
            )

            SectionHeader(
                title = "Study Statistics"
            )

            StatCard(
                title = "Completed tasks",
                value = "12"
            )

            StatCard(
                title = "Tasks in progress",
                value = "5"
            )

            StatCard(
                title = "Subjects",
                value = "6"
            )
        }
    }
}

@Composable
fun TaskCard(
    task: StudyTask,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(
            modifier = Modifier.padding(Spacing.Medium)
        ) {

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(
                    Spacing.Small
                )
            )

            Text(
                text = task.subject,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Due: ${task.deadline}",
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onFavoriteClick) {
                Text(if (isFavorite) "★ Favorite" else "☆ Add favorite")
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(category)
        }
    )
}

@Composable
fun SectionHeader(
    title: String
) {

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(
            vertical = Spacing.Medium
        )
    )
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun StatCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Medium),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun DetailScreenPreview() {

    StudyFlowTheme {
        DetailScreen(
            task = previewTasks.first(),
            isFavorite = false,
            onFavoriteClick = {},
            onBackClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {

    StudyFlowTheme {
        ProfileScreen(
            onBackClick = {}
        )
    }
}

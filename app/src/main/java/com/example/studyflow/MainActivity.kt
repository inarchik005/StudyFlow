package com.example.studyflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.studyflow.ui.theme.StudyFlowTheme

data class StudyTask(
    val id: Int,
    val title: String,
    val subject: String,
    val deadline: String,
    val description: String,
    val priority: String
)

val studyTasks = listOf(
    StudyTask(
        1,
        "Complete Android Development homework",
        "Android",
        "Oct 6",
        "Finish the Jetpack Compose screen and prepare the project for submission.",
        "High"
    ),
    StudyTask(
        2,
        "Review Kotlin basics",
        "Programming",
        "Oct 7",
        "Review variables, functions, collections and null safety in Kotlin.",
        "Medium"
    ),
    StudyTask(
        3,
        "Prepare Deep Learning assignment",
        "AI",
        "Oct 8",
        "Complete the required exercises and check the final notebook.",
        "High"
    ),
    StudyTask(
        4,
        "Study Chinese vocabulary",
        "Languages",
        "Oct 9",
        "Learn the new vocabulary and practice pronunciation.",
        "Low"
    ),
    StudyTask(
        5,
        "Complete Statistics exercises",
        "Math",
        "Oct 10",
        "Solve the assigned statistical problems and check the answers.",
        "Medium"
    ),
    StudyTask(
        6,
        "Review Cybersecurity lecture",
        "Cybersecurity",
        "Oct 11",
        "Review the lecture notes and write down important concepts.",
        "Medium"
    ),
    StudyTask(
        7,
        "Work on Information Systems project",
        "University",
        "Oct 12",
        "Update the project documentation and prepare the presentation.",
        "High"
    ),
    StudyTask(
        8,
        "Prepare presentation slides",
        "University",
        "Oct 13",
        "Create slides and organize the main points for the presentation.",
        "Medium"
    ),
    StudyTask(
        9,
        "Practice Git commands",
        "Programming",
        "Oct 14",
        "Practice commits, branches, merging and pushing changes to GitHub.",
        "Low"
    ),
    StudyTask(
        10,
        "Review Operating Systems",
        "University",
        "Oct 15",
        "Review processes, memory management and file system concepts.",
        "Medium"
    )
)

object Spacing {
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            StudyFlowTheme {
                StudyFlowApp()
            }
        }
    }
}

@Composable
fun StudyFlowApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                tasks = studyTasks,
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

            val taskId =
                backStackEntry.arguments?.getInt("taskId") ?: 1

            val task =
                studyTasks.firstOrNull { it.id == taskId }

            if (task != null) {
                DetailScreen(
                    task = task,
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
fun HomeScreen(
    tasks: List<StudyTask>,
    onTaskClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {

    var selectedCategory by remember {
        mutableStateOf("All")
    }

    val categories = listOf(
        "All",
        "Math",
        "Programming",
        "Android",
        "University",
        "AI"
    )

    val filteredTasks =
        if (selectedCategory == "All") {
            tasks
        } else {
            tasks.filter {
                it.subject == selectedCategory
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "StudyFlow",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(
                        onClick = onProfileClick
                    ) {
                        Text(
                            text = "👤",
                            style = MaterialTheme.typography.titleLarge
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
                .padding(horizontal = Spacing.Medium)
        ) {

            SectionHeader(
                title = "Categories"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(
                    Spacing.Small
                )
            ) {

                items(categories) { category ->

                    CategoryChip(
                        category = category,
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                        }
                    )
                }
            }

            SectionHeader(
                title = "Your tasks"
            )

            if (filteredTasks.isEmpty()) {

                EmptyState()

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(
                        Spacing.Small
                    )
                ) {

                    items(
                        items = filteredTasks,
                        key = { it.id }
                    ) { task ->

                        TaskCard(
                            task = task,
                            onClick = {
                                onTaskClick(task.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    task: StudyTask,
    onBackClick: () -> Unit
) {

    var isFavorite by remember {
        mutableStateOf(false)
    }

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
                onClick = {
                    isFavorite = !isFavorite
                },
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
    onClick: () -> Unit
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

@Preview(
    showBackground = true
)
@Composable
fun HomeScreenPreview() {

    StudyFlowTheme {
        HomeScreen(
            tasks = studyTasks,
            onTaskClick = {},
            onProfileClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun DetailScreenPreview() {

    StudyFlowTheme {
        DetailScreen(
            task = studyTasks.first(),
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

@Preview(
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {

    StudyFlowTheme {
        HomeScreen(
            tasks = studyTasks,
            onTaskClick = {},
            onProfileClick = {}
        )
    }
}
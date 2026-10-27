
package com.example.studyflow.data

import kotlinx.coroutines.delay

class TaskRepository {

    private val tasks = listOf(
        StudyTask(
            id = 1,
            title = "Complete Android Development homework",
            subject = "Android",
            deadline = "Oct 6",
            description = "Finish the Jetpack Compose screen and prepare the project for submission.",
            priority = "High"
        ),
        StudyTask(
            id = 2,
            title = "Review Kotlin basics",
            subject = "Programming",
            deadline = "Oct 7",
            description = "Review variables, functions, collections and null safety in Kotlin.",
            priority = "Medium"
        ),
        StudyTask(
            id = 3,
            title = "Prepare Deep Learning assignment",
            subject = "AI",
            deadline = "Oct 8",
            description = "Complete the required exercises and check the final notebook.",
            priority = "High"
        ),
        StudyTask(
            id = 4,
            title = "Study Chinese vocabulary",
            subject = "Languages",
            deadline = "Oct 9",
            description = "Learn the new vocabulary and practice pronunciation.",
            priority = "Low"
        ),
        StudyTask(
            id = 5,
            title = "Complete Statistics exercises",
            subject = "Math",
            deadline = "Oct 10",
            description = "Solve the assigned statistical problems and check the answers.",
            priority = "Medium"
        ),
        StudyTask(
            id = 6,
            title = "Review Cybersecurity lecture",
            subject = "Cybersecurity",
            deadline = "Oct 11",
            description = "Review the lecture notes and write down important concepts.",
            priority = "Medium"
        ),
        StudyTask(
            id = 7,
            title = "Work on Information Systems project",
            subject = "University",
            deadline = "Oct 12",
            description = "Update the project documentation and prepare the presentation.",
            priority = "High"
        ),
        StudyTask(
            id = 8,
            title = "Prepare presentation slides",
            subject = "University",
            deadline = "Oct 13",
            description = "Create slides and organize the main points for the presentation.",
            priority = "Medium"
        ),
        StudyTask(
            id = 9,
            title = "Practice Git commands",
            subject = "Programming",
            deadline = "Oct 14",
            description = "Practice commits, branches, merging and pushing changes to GitHub.",
            priority = "Low"
        ),
        StudyTask(
            id = 10,
            title = "Review Operating Systems",
            subject = "University",
            deadline = "Oct 15",
            description = "Review processes, memory management and file system concepts.",
            priority = "Medium"
        )
    )

    suspend fun getTasks(
        shouldFail: Boolean = false
    ): List<StudyTask> {
        delay(1500)

        if (shouldFail) {
            throw Exception("Failed to load tasks")
        }

        return tasks
    }
}

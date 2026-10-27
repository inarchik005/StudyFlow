# StudyFlow — MVVM Architecture

## 1. Screens (View)

- HomeScreen — displays task cards, categories, and the list of tasks.
- DetailScreen — displays details of the selected task.
- ProfileScreen — displays the user's profile.

## 2. ViewModel

- HomeViewModel — manages the home screen state and user actions.

## 3. UiState

HomeUiState contains:

- tasks — list of tasks to display
- searchQuery — current search text
- selectedCategory — currently selected category
- favoriteTaskIds — IDs of favorite tasks
- isLoading — whether data is loading
- error — error message, if loading fails

## 4. User Events

- onSearchChange(query)
- onCategorySelected(category)
- onFavouriteClick(taskId)
- onRetry()

## 5. Repository

TaskRepository owns the task data. It loads tasks asynchronously, simulates network delay, and can simulate a loading error.

## 6. Trace One Click

When the user selects a category, the screen calls onCategorySelected(category). The event is handled by HomeViewModel, which updates selectedCategory in HomeUiState using copy(). The UI collects the updated state and displays the tasks for the selected category.

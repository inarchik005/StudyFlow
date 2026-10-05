# AI Usage

## AI tools used

During the development of StudyFlow, I used AI assistants mainly for troubleshooting, understanding Android Studio errors, and improving the implementation.

The main AI tool I used was ChatGPT.

## Useful prompts

### 1. Debugging Gradle errors
"Why does my Android Studio project show Unexpected symbol errors in build.gradle.kts? Help me identify and fix the problem."

This helped me understand that the Gradle configuration had syntax/configuration problems.

### 2. Android Jetpack Compose navigation
"Help me implement navigation between HomeScreen, DetailScreen and ProfileScreen using Jetpack Compose Navigation."

This helped me understand how to connect different screens and navigate between them.

### 3. UI implementation
"Help me create a clean student task management interface in Jetpack Compose with categories, task cards, and a profile screen."

This helped me structure the UI and organize the Compose components.

## Example of an AI mistake

One issue happened when AI-generated code used the `study_flow_icon` resource before the resource was correctly available in the project. Android Studio showed an unresolved reference error.

I fixed this myself by creating the required `study_flow_icon.xml` file inside the `res/drawable` directory and then rebuilding the project.

After that, the project built successfully and the application ran correctly on the Android emulator.

## My contribution

I did not rely on AI to make the whole project automatically. I implemented, tested, and debugged the application in Android Studio myself.

I checked the generated code, fixed errors, created the application icon, tested the screens on the emulator, and used Git commits to track my work.
package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen

/**
 * Object that defines all the navigation routes (unique identifiers for each screen).
 * These constants are used by the NavHost and composable destinations to navigate
 * between screens in a type-safe and readable way.
 */
object Routes {
    // --- Student routes ---
    const val STUDENT_LIST = "student_list"
    const val STUDENT_FORM = "student_form"
    const val STUDENT_DETAIL = "student_detail/{studentId}"

    // --- Course routes ---
    const val COURSE_LIST = "course_list"
    const val COURSE_FORM = "course_form"
    const val COURSE_DETAIL = "course_detail/{courseId}"

    // --- Subscription routes ---
    const val SUBSCRIBE_LIST = "subscribe_list"
    const val SUBSCRIBE_FORM = "subscribe_form"
}

/**
 * Main navigation graph of the application.
 *
 * Purpose:
 * - Defines all possible navigation paths (screens) within the app.
 * - Uses Jetpack Compose Navigation to manage transitions between Composable screens.
 * - Keeps the UI reactive: when the user navigates, only the visible Composable is recomposed.
 *
 * Data flow:
 * - Navigation parameters (e.g., studentId) are passed via route arguments.
 * - The corresponding screen retrieves this argument from NavBackStackEntry.
 */
@Composable
fun AppNavHost() {
    // NavController: central object that controls navigation actions
    val navController = rememberNavController()

    /**
     * NavHost:
     * - Connects the NavController with the navigation graph (set of composable destinations).
     * - startDestination: the first screen shown when the app starts.
     * - Each "composable" block defines a screen and its route.
     */
    NavHost(navController, startDestination = Routes.SUBSCRIBE_LIST) {

        // -------------------------
        // Student navigation graph
        // -------------------------
        composable(Routes.STUDENT_LIST) {
            /**
             * StudentListScreen:
             * - Displays list of students (data observed via StateFlow).
             * - onNavigateToForm → navigates to the form screen for adding a new student.
             * - onNavigateToDetail → navigates to detail screen using student ID.
             */
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") }
            )
        }

        composable(Routes.STUDENT_FORM) {
            /**
             * StudentFormScreen:
             * - Allows creating or editing a student.
             * - After saving, navigates back to the previous screen using popBackStack().
             */
            StudentFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            "student_detail/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve studentId argument passed in navigation
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0

            /**
             * StudentDetailScreen:
             * - Shows details of a specific student.
             * - onBack → returns to previous screen.
             */
            StudentDetailScreen(studentId = id, onBack = { navController.popBackStack() })
        }

        // -------------------------
        // Course navigation graph
        // -------------------------
        composable(Routes.COURSE_LIST) {
            /**
             * CourseListScreen:
             * - Displays all available courses fetched from the database.
             * - Works similarly to StudentListScreen but for Course entities.
             * - Uses a ViewModel internally (via Hilt) to observe the list of courses as a StateFlow.
             * - collectAsState() ensures the UI recomposes automatically whenever the list changes.
             *
             * Navigation callbacks:
             * - onNavigateToForm → navigates to the course form screen to add a new course.
             * - onNavigateToDetail → navigates to a detailed view for the selected course (using its ID).
             */
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") }
            )
        }

        composable(Routes.COURSE_FORM) {
            /**
             * CourseFormScreen:
             * - Form used to create or edit a CourseEntity.
             * - After saving, popBackStack() navigates back to the course list.
             */
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }

        composable(
            "course_detail/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Retrieve courseId argument passed during navigation
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0

            /**
             * CourseDetailScreen:
             * - Displays detailed information about a specific course.
             * - onBack navigates back to the list using popBackStack().
             */
            CourseDetailScreen(courseId = id, onBack = { navController.popBackStack() })
        }

        // -------------------------
        // Subscription navigation graph
        // -------------------------
        composable(Routes.SUBSCRIBE_LIST) {
            /**
             * SubscribeListScreen:
             * - Displays all course subscriptions.
             * - onNavigateToForm → navigates to the form for creating a new subscription.
             */
            SubscribeListScreen(onNavigateToForm = { navController.navigate(Routes.SUBSCRIBE_FORM) })
        }

        composable(Routes.SUBSCRIBE_FORM) {
            /**
             * SubscribeFormScreen:
             * - Form for adding a new subscription between student and course.
             * - onSaved → returns to subscription list.
             */
            SubscribeFormScreen(onSaved = { navController.popBackStack() })
        }
    }
}

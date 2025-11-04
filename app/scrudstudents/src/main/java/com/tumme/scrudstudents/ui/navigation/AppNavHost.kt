package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.tumme.scrudstudents.ui.screens.auth.LoginScreen
import com.tumme.scrudstudents.ui.screens.auth.RegisterScreen
import com.tumme.scrudstudents.ui.screens.auth.SplashScreen
import com.tumme.scrudstudents.ui.screens.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.screens.student.StudentCourseListScreen
import com.tumme.scrudstudents.ui.screens.student.StudentGradesScreen
import com.tumme.scrudstudents.ui.screens.student.StudentSubscribeScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherCourseListScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherGradeEntryScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherHomeScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherStudentListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash - decides where to go (login or direct)
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                } }
            )
        }

        // --- Login ---
        composable(Routes.LOGIN) {
            LoginScreen(
                onStudentLogin = { userId ->
                    navController.navigate("${Routes.STUDENT_HOME}/$userId") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onTeacherLogin = { userId ->
                    navController.navigate("${Routes.TEACHER_HOME}/$userId") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) { popUpTo(Routes.REGISTER) { inclusive = true } }
                }
            )
        }

        // Student
        composable("${Routes.STUDENT_HOME}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            StudentHomeScreen(
                userId = userId,
                onNavigateToCourses = { userId?.let { navController.navigate("${Routes.STUDENT_COURSE}/$it") } },
                onNavigateToSubscriptions = { userId?.let { navController.navigate("${Routes.STUDENT_SUBSCRIBE}/$it") } },
                onNavigateToGrades = { userId?.let { navController.navigate("${Routes.STUDENT_GRADES}/$it") } },
                onLogoutNavigate = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.STUDENT_HOME) { inclusive = true }
                    }
                }
            )
        }

        composable("${Routes.STUDENT_COURSE}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            StudentCourseListScreen(userId = userId)
        }

        composable("${Routes.STUDENT_SUBSCRIBE}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            StudentSubscribeScreen(userId = userId)
        }

        composable("${Routes.STUDENT_GRADES}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            StudentGradesScreen(userId = userId)
        }

        // Teacher Home
        composable("${Routes.TEACHER_HOME}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            TeacherHomeScreen(
                userId = userId,
                onNavigateToDeclareCourses = { navController.navigate("${Routes.TEACHER_COURSE_LIST}/$it") },
                onNavigateToEnterGrades = { navController.navigate("${Routes.TEACHER_GRADE_ENTRY}/$it") },
                onNavigateToViewEnrolledStudents = { navController.navigate("${Routes.TEACHER_STUDENT_LIST}/$it") },
                onLogoutNavigate = { navController.navigate(Routes.LOGIN) { popUpTo(Routes.TEACHER_HOME) { inclusive = true } } }
            )
        }

        composable("${Routes.TEACHER_COURSE_LIST}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            TeacherCourseListScreen(userId = userId)
        }

        composable("${Routes.TEACHER_STUDENT_LIST}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            TeacherStudentListScreen(userId = userId)
        }

        composable("${Routes.TEACHER_GRADE_ENTRY}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            TeacherGradeEntryScreen(userId = userId)
        }
    }
}

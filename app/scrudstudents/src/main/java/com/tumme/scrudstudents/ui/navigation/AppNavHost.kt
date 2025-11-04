package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.tumme.scrudstudents.ui.screens.auth.LoginScreen
import com.tumme.scrudstudents.ui.screens.auth.RegisterScreen
import com.tumme.scrudstudents.ui.screens.auth.SplashScreen
import com.tumme.scrudstudents.ui.screens.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherHomeScreen

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

        // Student Home
        composable("${Routes.STUDENT_HOME}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            StudentHomeScreen(
                userId = userId,
                onLogoutNavigate = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.STUDENT_HOME) { inclusive = true }
                    }
                }
            )
        }

        // Teacher Home
        composable("${Routes.TEACHER_HOME}/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            TeacherHomeScreen(
                userId = userId,
                onLogoutNavigate = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.TEACHER_HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}

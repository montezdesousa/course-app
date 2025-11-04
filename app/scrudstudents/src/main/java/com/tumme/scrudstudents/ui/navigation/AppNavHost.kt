package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.screens.auth.LoginScreen
import com.tumme.scrudstudents.ui.screens.auth.RegisterScreen
import com.tumme.scrudstudents.ui.screens.auth.SplashScreen
import com.tumme.scrudstudents.ui.screens.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.screens.student.StudentCourseListScreen
import com.tumme.scrudstudents.ui.screens.student.StudentFinalGradeSummaryScreen
import com.tumme.scrudstudents.ui.screens.student.StudentGradesScreen
import com.tumme.scrudstudents.ui.screens.student.StudentSubscribeScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherCourseListScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherGradeEntryScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherHomeScreen
import com.tumme.scrudstudents.ui.screens.teacher.TeacherStudentListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onStudentLogin = {
                    navController.navigate(Routes.STUDENT_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onTeacherLogin = {
                    navController.navigate(Routes.TEACHER_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Register
        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }

        // Student
        composable(Routes.STUDENT_HOME) {
            StudentHomeScreen(
                authViewModel = authViewModel,
                onNavigateToCourses = { navController.navigate(Routes.STUDENT_COURSE) },
                onNavigateToSubscriptions = { navController.navigate(Routes.STUDENT_SUBSCRIBE) },
                onNavigateToGrades = { navController.navigate(Routes.STUDENT_GRADES) },
                onNavigateToFinalGradeSummary = {navController.navigate(Routes.STUDENT_FINAL_GRADE_SUMMARY)},
                onLogoutNavigate = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.STUDENT_HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.STUDENT_COURSE) {
            StudentCourseListScreen(
                authViewModel = authViewModel,
            )
        }

        composable(Routes.STUDENT_SUBSCRIBE) {
            StudentSubscribeScreen(
                authViewModel = authViewModel,
            )
        }

        composable(Routes.STUDENT_GRADES) {
            StudentGradesScreen(
                authViewModel = authViewModel,
            )
        }

        composable(Routes.STUDENT_FINAL_GRADE_SUMMARY) {
            StudentFinalGradeSummaryScreen(
                authViewModel = authViewModel,
            )
        }

        // Teacher
        composable(Routes.TEACHER_HOME) {
            TeacherHomeScreen(
                authViewModel = authViewModel,
                onNavigateToCourses = { navController.navigate(Routes.TEACHER_COURSE_LIST) },
                onNavigateToGrades = { navController.navigate(Routes.TEACHER_GRADE_ENTRY) },
                onNavigateToEnrolledStudents = { navController.navigate(Routes.TEACHER_STUDENT_LIST) },
                onLogoutNavigate = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.TEACHER_HOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.TEACHER_COURSE_LIST) {
            TeacherCourseListScreen(
                authViewModel = authViewModel,
            )
        }

        composable(Routes.TEACHER_STUDENT_LIST) {
            TeacherStudentListScreen(
                authViewModel = authViewModel,
            )
        }

        composable(Routes.TEACHER_GRADE_ENTRY) {
            TeacherGradeEntryScreen(
                authViewModel = authViewModel,
            )
        }
    }
}

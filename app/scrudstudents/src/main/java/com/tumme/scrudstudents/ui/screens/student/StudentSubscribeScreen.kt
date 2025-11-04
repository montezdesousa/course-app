package com.tumme.scrudstudents.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSubscribeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    studentCourseViewModel: StudentViewModel = hiltViewModel(),
    onLogoutNavigate: () -> Unit = {}
) {
    val studentId = authViewModel.currentUserId ?: return
    val studentLevel = authViewModel.currentUserLevelOfStudy ?: LevelCourse.P1

    // Courses student is enrolled in
    val enrolledCourses by studentCourseViewModel.getEnrolledCourses(studentId)
        .collectAsState(initial = emptyList())

    // Courses available to enroll (filtered by student's level)
    val availableCourses by studentCourseViewModel.getAvailableCourses(studentId, studentLevel)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Enrolled Courses ---
        Text("My Enrolled Courses", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (enrolledCourses.isEmpty()) {
            Text("You are not enrolled in any courses yet.")
        } else {
            LazyColumn(modifier = Modifier.height(200.dp)) {
                items(enrolledCourses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${course.name}")
                            Text("ECTS: ${course.ects}")
                            Text("Level: ${course.level.value}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Available Courses to Enroll ---
        Text("Available Courses for Your Level", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (availableCourses.isEmpty()) {
            Text("No courses available for enrollment at your level.")
        } else {
            LazyColumn(modifier = Modifier.height(200.dp)) {
                items(availableCourses) { course ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                // Enroll student in course
                                studentCourseViewModel.enrollStudent(studentId, course.idCourse)
                            },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${course.name}")
                            Text("ECTS: ${course.ects}")
                            Text("Level: ${course.level.value}")
                            Text("Click to enroll", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Logout ---
        Button(
            onClick = {
                authViewModel.logout()
                onLogoutNavigate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

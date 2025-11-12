package com.tumme.scrudstudents.ui.screens.teacher

import com.tumme.scrudstudents.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToCourses: () -> Unit = {},
    onNavigateToGrades: () -> Unit = {},
    onNavigateToEnrolledStudents: () -> Unit = {},
    onLogoutNavigate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.teacher_welcome, authViewModel.currentUsername ?: ""),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Declare Courses
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToCourses() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.student_courses))
            }
        }

        // Enter Grades
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToGrades() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.teacher_grades))
            }
        }

        // View Enrolled Students
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToEnrolledStudents() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.teacher_enrolled_students))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = {
                authViewModel.logout()
                onLogoutNavigate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.logout))
        }
    }
}

package com.tumme.scrudstudents.ui.screens.student

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
import com.tumme.scrudstudents.ui.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCourseListScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel = hiltViewModel(),
) {
    val studentLevel = authViewModel.currentUserLevelOfStudy ?: LevelCourse.P1

    // Fetch courses filtered by student level
    val courses by courseViewModel.getCoursesByLevel(studentLevel)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Courses for ${studentLevel.value}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (courses.isEmpty()) {
            Text("No courses available for your level.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { course ->
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
    }
}

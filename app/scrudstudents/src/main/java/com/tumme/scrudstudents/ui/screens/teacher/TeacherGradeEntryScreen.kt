package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel
import com.tumme.scrudstudents.ui.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherGradeEntryScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    onLogoutNavigate: () -> Unit = {}
) {
    val teacherId = authViewModel.currentUserId ?: return

    // Teacher's courses
    val courses by teacherViewModel.getCoursesForTeacher(teacherId).collectAsState()

    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }

    // Students enrolled in the selected course
    val enrolledSubscribes by subscribeViewModel
        .getSubscribesByCourse(selectedCourse?.idCourse ?: -1)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Logout Button ---
        Button(
            onClick = {
                authViewModel.logout()
                onLogoutNavigate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Select Course Dropdown ---
        var courseDropdownExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = courseDropdownExpanded,
            onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedCourse?.name ?: "Select Course",
                onValueChange = {},
                readOnly = true,
                label = { Text("Course") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(courseDropdownExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = courseDropdownExpanded,
                onDismissRequest = { courseDropdownExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.name) },
                        onClick = {
                            selectedCourse = course
                            courseDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Students Table ---
        Text("Enrolled Students", style = MaterialTheme.typography.titleMedium)
        if (selectedCourse == null) {
            Text("Select a course to view students.")
        } else if (enrolledSubscribes.isEmpty()) {
            Text("No students enrolled in this course.")
        } else {
            LazyColumn {
                items(enrolledSubscribes) { subscribe ->
                    var grade by remember { mutableStateOf(subscribe.score.toString()) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Student ID: ${subscribe.studentId}")

                            OutlinedTextField(
                                value = grade,
                                onValueChange = { grade = it },
                                label = { Text("Grade") },
                                modifier = Modifier.width(100.dp),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    val scoreFloat = grade.toFloatOrNull() ?: 0f
                                    subscribeViewModel.updateSubscribeScore(
                                        subscribe.copy(score = scoreFloat),
                                        scoreFloat
                                    )
                                }
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherCourseListScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onLogoutNavigate: () -> Unit = {}
) {
    val userId = authViewModel.currentUserId

    if (userId == null) {
        Text("No user logged in.")
        return
    }

    // Collect courses for this teacher
    val courses by teacherViewModel.getCoursesForTeacher(userId).collectAsState(initial = emptyList())

    // Dialog and form states
    var showDialog by remember { mutableStateOf(false) }
    var newCourseName by remember { mutableStateOf("") }
    var newCourseEcts by remember { mutableStateOf("") }
    var newCourseLevel by remember { mutableStateOf(LevelCourse.P1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Courses") },
                actions = {
                    TextButton(onClick = onLogoutNavigate) {
                        Text("Logout", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add course")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (courses.isEmpty()) {
                Text("No courses yet. Click + to add one.")
            } else {
                LazyColumn {
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

    // --- Add Course Dialog ---
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add New Course") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newCourseName,
                        onValueChange = { newCourseName = it },
                        label = { Text("Course Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCourseEcts,
                        onValueChange = { newCourseEcts = it },
                        label = { Text("ECTS") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Level Dropdown
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedTextField(
                            value = newCourseLevel.value,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = true },
                            label = { Text("Level") }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            LevelCourse.entries.forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level.value) },
                                    onClick = {
                                        newCourseLevel = level
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    // Add the new course assigned to the current teacher
                    val course = CourseEntity(
                        name = newCourseName,
                        ects = newCourseEcts.toFloatOrNull() ?: 0f,
                        level = newCourseLevel,
                        teacherId = userId
                    )
                    teacherViewModel.addCourse(course)

                    newCourseName = ""
                    newCourseEcts = ""
                    newCourseLevel = LevelCourse.P1
                    showDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

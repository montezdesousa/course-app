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
    teacherViewModel: TeacherViewModel = hiltViewModel()
) {
    val userId = authViewModel.currentUserId ?: return
    val courses by teacherViewModel.getCoursesForTeacher(userId).collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var newCourseName by remember { mutableStateOf("") }
    var newCourseEcts by remember { mutableStateOf("") }
    var newCourseLevel by remember { mutableStateOf(LevelCourse.P1) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("My Courses") }) },
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

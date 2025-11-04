package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
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
                TableHeader(
                    cells = listOf("Name", "ECTS", "Level", "Actions"),
                    weights = listOf(0.4f, 0.2f, 0.2f, 0.2f)
                )
                Spacer(Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(courses) { course ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(course.name, modifier = Modifier.weight(0.4f))
                            Text(course.ects.toString(), modifier = Modifier.weight(0.2f))
                            Text(course.level.value, modifier = Modifier.weight(0.2f))
                            IconButton(
                                onClick = { teacherViewModel.deleteCourse(course) },
                                modifier = Modifier.weight(0.2f)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete course")
                            }
                        }
                        Divider()
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

                    // Fixed dropdown inside dialog
                    var expanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = newCourseLevel.value,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Level") },
                            trailingIcon = {
                                IconButton(onClick = { expanded = !expanded }) {
                                    Icon(Icons.Default.Add, contentDescription = "Select level")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
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
                TextButton(onClick = { showDialog = false }) { Text("Cancel") }
            }
        )
    }
}

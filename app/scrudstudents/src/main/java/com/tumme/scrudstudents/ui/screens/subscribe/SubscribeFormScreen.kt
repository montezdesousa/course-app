package com.tumme.scrudstudents.ui.screens.subscribe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Save
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel

@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onSaved: () -> Unit = {}
) {
    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()

    var selectedStudent by remember { mutableStateOf<Int?>(students.firstOrNull()?.idStudent) }
    var selectedCourse by remember { mutableStateOf<Int?>(courses.firstOrNull()?.idCourse) }
    var score by remember { mutableStateOf("") }

    var scoreError by remember { mutableStateOf(false) }
    var studentError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf(false) }

    var showStudentMenu by remember { mutableStateOf(false) }
    var showCourseMenu by remember { mutableStateOf(false) }

    // Set default selection when list updates
    LaunchedEffect(students) {
        if (selectedStudent == null && students.isNotEmpty()) {
            selectedStudent = students.first().idStudent
        }
    }
    LaunchedEffect(courses) {
        if (selectedCourse == null && courses.isNotEmpty()) {
            selectedCourse = courses.first().idCourse
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp) // Outer padding
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Student dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = students.find { it.idStudent == selectedStudent }?.let { "${it.firstName} ${it.lastName}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Student") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showStudentMenu) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Transparent clickable layer on top of TextField
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showStudentMenu = !showStudentMenu }
                )

                DropdownMenu(
                    expanded = showStudentMenu,
                    onDismissRequest = { showStudentMenu = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    students.forEach { s ->
                        DropdownMenuItem(
                            text = { Text("${s.firstName} ${s.lastName}") },
                            onClick = {
                                selectedStudent = s.idStudent
                                showStudentMenu = false
                            }
                        )
                    }
                }
            }

            if (studentError) Text(
                text = "Select a student",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))

            // Course dropdown
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = courses.find { it.idCourse == selectedCourse }?.nameCourse ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Course") },
                    trailingIcon = {
                        Icon(
                            imageVector = if (showCourseMenu) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Transparent clickable layer on top of TextField
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { showCourseMenu = !showCourseMenu }
                )

                DropdownMenu(
                    expanded = showCourseMenu,
                    onDismissRequest = { showCourseMenu = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    courses.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c.nameCourse) },
                            onClick = {
                                selectedCourse = c.idCourse
                                showCourseMenu = false
                            }
                        )
                    }
                }
            }

            if (courseError) Text(
                text = "Select a course",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(8.dp))

            // Score input
            TextField(
                value = score,
                onValueChange = {
                    score = it
                    val f = it.toFloatOrNull()
                    scoreError = f == null || f < 0f
                },
                label = { Text("Score") },
                isError = scoreError,
                modifier = Modifier.fillMaxWidth()
            )
            if (scoreError) Text(
                text = "Enter a valid positive score",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(16.dp))

            // Save Button
            Button(onClick = {
                studentError = selectedStudent == null
                courseError = selectedCourse == null
                val scoreFloat = score.toFloatOrNull()
                scoreError = scoreFloat == null || scoreFloat < 0f

                if (studentError || courseError || scoreError) return@Button

                val subscribe = SubscribeEntity(
                    studentId = selectedStudent!!,
                    courseId = selectedCourse!!,
                    score = scoreFloat!!
                )
                viewModel.insertSubscribe(subscribe)
                onSaved()
            }) {
                Icon(Icons.Default.Save, contentDescription = "Save subscription")
                Spacer(Modifier.width(8.dp))
                Text("Save")
            }
        }
    }
}

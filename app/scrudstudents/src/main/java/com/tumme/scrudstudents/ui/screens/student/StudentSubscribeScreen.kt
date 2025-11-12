package com.tumme.scrudstudents.ui.screens.student

import com.tumme.scrudstudents.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSubscribeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
) {
    val studentId = authViewModel.currentUserId ?: return
    val studentLevel = authViewModel.currentUserLevelOfStudy ?: LevelCourse.P1

    val enrolledCourses by subscribeViewModel.getEnrolledCourses(studentId)
        .collectAsState(initial = emptyList())

    val availableCourses by subscribeViewModel.getAvailableCourses(studentId, studentLevel)
        .collectAsState(initial = emptyList())

    var showEnrollDialog by remember { mutableStateOf(false) }
    var selectedCourseForEnroll by remember { mutableStateOf<SubscribeEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.student_my_subscriptions)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showEnrollDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.student_enroll_in_new_course))
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.student_my_courses), style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (enrolledCourses.isEmpty()) {
                Text(stringResource(R.string.student_not_enrolled))
            } else {
                TableHeader(
                    cells = listOf(
                        stringResource(R.string.course_name),
                        stringResource(R.string.course_ects),
                        stringResource(R.string.course_level),
                        stringResource(R.string.action)
                    ),
                    weights = listOf(0.45f, 0.2f, 0.2f, 0.15f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(enrolledCourses, key = { it.idCourse }) { course ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(course.name, modifier = Modifier.weight(0.45f))
                            Text(course.ects.toString(), modifier = Modifier.weight(0.2f))
                            Text(course.level.value, modifier = Modifier.weight(0.2f))
                            IconButton(
                                onClick = {
                                    val subscribe = SubscribeEntity(
                                        studentId = studentId,
                                        courseId = course.idCourse,
                                    )
                                    subscribeViewModel.deleteSubscribe(subscribe)
                                },
                                modifier = Modifier.weight(0.15f)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.unenroll))
                            }
                        }
                        Divider()
                    }
                }
            }

            if (showEnrollDialog) {
                AlertDialog(
                    onDismissRequest = { showEnrollDialog = false },
                    title = { Text(stringResource(R.string.enroll_in_a_course)) },
                    text = {
                        Column {
                            var expanded by remember { mutableStateOf(false) }
                            var selectedCourse by remember { mutableStateOf("") }

                            OutlinedTextField(
                                value = selectedCourse,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.select_course)) },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.select_course))
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                availableCourses.forEach { course ->
                                    DropdownMenuItem(
                                        text = { Text(course.name) },
                                        onClick = {
                                            selectedCourse = course.name
                                            selectedCourseForEnroll = SubscribeEntity(
                                                studentId = studentId,
                                                courseId = course.idCourse,
                                            )
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            selectedCourseForEnroll?.let { subscribeViewModel.insertSubscribe(it) }
                            showEnrollDialog = false
                            selectedCourseForEnroll = null
                        }) { Text(stringResource(R.string.enroll)) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEnrollDialog = false }) { Text(stringResource(R.string.cancel)) }
                    }
                )
            }
        }
    }
}

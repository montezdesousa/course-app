package com.tumme.scrudstudents.ui.screens.teacher

import com.tumme.scrudstudents.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.StudentViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel
import com.tumme.scrudstudents.ui.viewmodel.TeacherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherGradeEntryScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    studentViewModel: StudentViewModel = hiltViewModel()
) {
    val teacherId = authViewModel.currentUserId ?: return
    val courses by teacherViewModel.getCoursesForTeacher(teacherId).collectAsState(initial = emptyList())

    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    var courseDropdownExpanded by remember { mutableStateOf(false) }

    val enrolledSubscribes by subscribeViewModel
        .getSubscribesByCourse(selectedCourse?.idCourse ?: -1)
        .collectAsState(initial = emptyList())

    // --- Map studentId -> StudentEntity ---
    val studentMap = remember { mutableStateMapOf<Int, com.tumme.scrudstudents.data.local.model.StudentEntity?>() }
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(selectedCourse, enrolledSubscribes) {
        enrolledSubscribes.forEach { subscribe ->
            if (!studentMap.containsKey(subscribe.studentId)) {
                coroutineScope.launch {
                    val student = studentViewModel.getStudentById(subscribe.studentId)
                    studentMap[subscribe.studentId] = student
                }
            }
        }
    }

    // --- State for popup ---
    var gradeDialogVisible by remember { mutableStateOf(false) }
    var gradeToEdit by remember { mutableStateOf("") }
    var subscribeToEdit by remember { mutableStateOf<com.tumme.scrudstudents.data.local.model.SubscribeEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Course Dropdown ---
        ExposedDropdownMenuBox(
            expanded = courseDropdownExpanded,
            onExpandedChange = { courseDropdownExpanded = !courseDropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCourse?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.teacher_course)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseDropdownExpanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = courseDropdownExpanded,
                onDismissRequest = { courseDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
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
        Text(stringResource(R.string.teacher_enrolled_students), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedCourse == null) {
            Text(stringResource(R.string.teacher_select_course_to_view_students))
        } else if (enrolledSubscribes.isEmpty()) {
            Text(stringResource(R.string.teacher_no_students_enrolled))
        } else {
            TableHeader(
                cells = listOf(
                    stringResource(R.string.student_id),
                    stringResource(R.string.register_first_name),
                    stringResource(R.string.register_last_name),
                    stringResource(R.string.student_grade),
                    stringResource(R.string.actions)
                ),
                weights = listOf(0.15f, 0.25f, 0.25f, 0.2f, 0.15f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(enrolledSubscribes) { subscribe ->
                    val student = studentMap[subscribe.studentId]
                    val currentGrade = subscribe.score

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(subscribe.studentId.toString(), modifier = Modifier.weight(0.15f))
                        Text(student?.firstName ?: "-", modifier = Modifier.weight(0.25f))
                        Text(student?.lastName ?: "-", modifier = Modifier.weight(0.25f))
                        Text(currentGrade?.toString() ?: "-", modifier = Modifier.weight(0.2f))
                        IconButton(
                            onClick = {
                                subscribeToEdit = subscribe
                                gradeToEdit = currentGrade?.toString() ?: ""
                                gradeDialogVisible = true
                            },
                            modifier = Modifier.weight(0.15f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_grade))
                        }
                    }
                    Divider()
                }
            }
        }
    }

    // --- Grade Edit Dialog ---
    if (gradeDialogVisible && subscribeToEdit != null) {
        AlertDialog(
            onDismissRequest = { gradeDialogVisible = false },
            title = { Text(stringResource(R.string.edit_grade)) },
            text = {
                OutlinedTextField(
                    value = gradeToEdit,
                    onValueChange = { gradeToEdit = it },
                    label = { Text(stringResource(R.string.student_grade)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val newScore = gradeToEdit.toFloatOrNull() ?: 0f
                    subscribeToEdit?.let { sub ->
                        subscribeViewModel.updateSubscribeScore(sub.copy(score = newScore), newScore)
                    }
                    gradeDialogVisible = false
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { gradeDialogVisible = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

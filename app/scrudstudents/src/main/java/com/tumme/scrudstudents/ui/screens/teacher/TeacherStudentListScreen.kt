package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
fun TeacherStudentListScreen(
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
                label = { Text("Course") },
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
        Text("Enrolled Students", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (selectedCourse == null) {
            Text("Select a course to view students.")
        } else if (enrolledSubscribes.isEmpty()) {
            Text("No students enrolled in this course.")
        } else {
            TableHeader(
                cells = listOf("ID", "First Name", "Last Name"),
                weights = listOf(0.2f, 0.4f, 0.4f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(enrolledSubscribes) { subscribe ->
                    val student = studentMap[subscribe.studentId]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(student?.idStudent?.toString() ?: "-", modifier = Modifier.weight(0.2f))
                        Text(student?.firstName ?: "-", modifier = Modifier.weight(0.4f))
                        Text(student?.lastName ?: "-", modifier = Modifier.weight(0.4f))
                    }
                    Divider()
                }
            }
        }
    }
}

package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel
import com.tumme.scrudstudents.ui.viewmodel.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentListScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateToGradeEntry: (studentId: Int, courseId: Int) -> Unit = { _, _ -> },
) {
    val teacherId = authViewModel.currentUserId ?: return

    // Fetch courses taught by this teacher
    val courses by teacherViewModel.getCoursesForTeacher(teacherId).collectAsState(emptyList())

    // Fetch all subscribes
    val subscribes by subscribeViewModel.subscriptions.collectAsState()

    // Map of courseId -> List<StudentEntity>
    val studentsByCourse = courses.associateWith { course ->
        subscribes.filter { it.courseId == course.idCourse }.mapNotNull { sub ->
            subscribeViewModel.students.value.find { it.idStudent == sub.studentId }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Enrolled Students", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            studentsByCourse.forEach { (course, students) ->
                item {
                    Text("Course: ${course.name}", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(students) { student ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onNavigateToGradeEntry(student.idStudent, course.idCourse) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("${student.firstName} ${student.lastName}")
                            Text("Click to enter grades", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

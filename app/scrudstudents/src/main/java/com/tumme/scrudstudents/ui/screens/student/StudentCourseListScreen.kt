package com.tumme.scrudstudents.ui.screens.student

import com.tumme.scrudstudents.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
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
    val courses by courseViewModel.getCoursesByLevel(studentLevel.value)
        .collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.student_courses_for_level, studentLevel.value),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (courses.isEmpty()) {
            Text(stringResource(R.string.student_no_courses_available))
        } else {
            TableHeader(
                cells = listOf(
                    stringResource(R.string.course_name),
                    stringResource(R.string.course_ects),
                    stringResource(R.string.course_level)
                ),
                weights = listOf(0.5f, 0.25f, 0.25f)
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses, key = { it.idCourse }) { course ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(course.name, modifier = Modifier.weight(0.5f))
                        Text(course.ects.toString(), modifier = Modifier.weight(0.25f))
                        Text(course.level.value, modifier = Modifier.weight(0.25f))
                    }
                    Divider()
                }
            }
        }
    }
}

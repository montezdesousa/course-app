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
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
) {
    val studentId = authViewModel.currentUserId ?: return
    val subscriptions by subscribeViewModel.getSubscribesByStudent(studentId)
        .collectAsState(initial = emptyList())
    val courses by subscribeViewModel.courses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.student_my_grades),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (subscriptions.isEmpty()) {
            Text(stringResource(R.string.student_not_enrolled))
        } else {
            // --- Table Header ---
            TableHeader(
                cells = listOf(
                    stringResource(R.string.course_name),
                    stringResource(R.string.course_level),
                    stringResource(R.string.student_grade)
                ),
                weights = listOf(0.5f, 0.25f, 0.25f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Table Rows ---
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(subscriptions, key = { it.courseId }) { sub ->
                    val course = courses.find { it.idCourse == sub.courseId }
                    if (course != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(course.name, modifier = Modifier.weight(0.5f))
                            Text(course.level.value, modifier = Modifier.weight(0.25f))
                            Text(
                                if (sub.score != null) sub.score.toString() else stringResource(R.string.student_not_graded_yet),
                                modifier = Modifier.weight(0.25f)
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

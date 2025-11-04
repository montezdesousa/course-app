package com.tumme.scrudstudents.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFinalGradeSummaryScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    subscribeViewModel: SubscribeViewModel = hiltViewModel(),
) {
    val studentId = authViewModel.currentUserId ?: return
    val subscriptions by subscribeViewModel.getSubscribesByStudent(studentId).collectAsState(initial = emptyList())
    val courses by subscribeViewModel.courses.collectAsState()

    val levelGrades = LevelCourse.entries.map { level ->
        val levelCourses = subscriptions.mapNotNull { sub ->
            courses.find { it.idCourse == sub.courseId && it.level == level }?.let { it to sub.score }
        }

        val totalEcts = levelCourses.sumOf { it.first.ects.toDouble() }
        val weightedSum = levelCourses.sumOf { it.first.ects * it.second.toDouble() }

        val average = if (totalEcts > 0) weightedSum / totalEcts else null
        level to average
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Final Grade Summary", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (subscriptions.isEmpty()) {
            Text("You are not enrolled in any courses yet.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(levelGrades) { (level, avg) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Level: ${level.value}")
                            Text("Final Grade: ${avg?.let { String.format("%.2f", it) } ?: "No grades yet"}")
                        }
                    }
                }
            }
        }
    }
}

package com.tumme.scrudstudents.ui.screens.student

import com.tumme.scrudstudents.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader
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

    // Filter subscriptions that have a matching course and a non-null score
    val gradedPairs = subscriptions.mapNotNull { sub ->
        val course = courses.find { it.idCourse == sub.courseId }
        val score = sub.score
        if (course != null && score != null) {
            course to score.toDouble()
        } else null
    }

    // Sum ECTS of graded courses
    val gradedEctsTotal = gradedPairs.sumOf { it.first.ects.toDouble() }

    // Weighted sum (ECTS * grade)
    val weightedSum = gradedPairs.sumOf { it.first.ects * it.second }

    // Weighted average
    val weightedAverage = if (gradedEctsTotal > 0.0) weightedSum / gradedEctsTotal else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.student_final_grade_summary), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (subscriptions.isEmpty()) {
            Text(stringResource(R.string.student_not_enrolled))
        } else {
            // Table header
            TableHeader(
                cells = listOf(
                    stringResource(R.string.graded_ects),
                    stringResource(R.string.weighted_average)
                ),
                weights = listOf(0.4f, 0.6f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Single row showing totals
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val ectsDisplay = if (gradedEctsTotal % 1.0 == 0.0) {
                    gradedEctsTotal.toInt().toString()
                } else {
                    String.format("%.2f", gradedEctsTotal)
                }
                Text(ectsDisplay, modifier = Modifier.weight(0.4f))

                Text(
                    text = weightedAverage?.let { String.format("%.2f", it) } ?: "-",
                    modifier = Modifier.weight(0.6f)
                )
            }
            Divider()
        }
    }
}

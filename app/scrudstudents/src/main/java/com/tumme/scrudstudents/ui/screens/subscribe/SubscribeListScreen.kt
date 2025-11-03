package com.tumme.scrudstudents.ui.screens.subscribe

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.tumme.scrudstudents.ui.viewmodel.SubscribeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {}
) {
    val subscriptions by viewModel.subscriptions.collectAsState()
    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Subscriptions") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Icon(Icons.Default.Add, contentDescription = "Add subscription")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("Student", "Course", "Score", "Actions"),
                weights = listOf(0.35f, 0.35f, 0.15f, 0.15f)
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(subscriptions) { s ->
                    val studentName = students.find { it.idStudent == s.studentId }?.let { "${it.firstName} ${it.lastName}" } ?: s.studentId.toString()
                    val courseName = courses.find { it.idCourse == s.courseId }?.nameCourse ?: s.courseId.toString()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(studentName, modifier = Modifier.weight(0.35f))
                        Text(courseName, modifier = Modifier.weight(0.35f))
                        Text(s.score.toString(), modifier = Modifier.weight(0.15f))
                        Row(modifier = Modifier.weight(0.15f), horizontalArrangement = Arrangement.SpaceEvenly) {
                            IconButton(onClick = { /* edit */ }) { /* optional edit icon */ }
                            IconButton(onClick = { viewModel.deleteSubscribe(s) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

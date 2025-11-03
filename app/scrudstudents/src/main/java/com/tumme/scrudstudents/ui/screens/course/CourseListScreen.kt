package com.tumme.scrudstudents.ui.screens.course

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
import com.tumme.scrudstudents.ui.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    viewModel: CourseViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val courses by viewModel.courses.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Courses") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) { // Navigate to form on FAB click
                Icon(Icons.Default.Add, contentDescription = "Add course") // Simple "+" icon
            }
        }
    ) { padding -> // Scaffold content padding
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding) // Account for top bar & FAB
            .padding(16.dp)             // Inner padding
        ) {
            // Table header
            TableHeader(cells = listOf("Name", "ECTS", "Level", "Actions"),
                weights = listOf(0.4f, 0.2f, 0.2f, 0.2f))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { course ->
                    CourseRow(
                        course = course,
                        onEdit = { /* navigate to form prefilled (not implemented here) */ },
                        onDelete = { viewModel.deleteCourse(course) },
                        onView = { onNavigateToDetail(course.idCourse) }, // Navigate to details
                        onShare = { /* share intent */ }
                    )
                }
            }
        }
    }
}

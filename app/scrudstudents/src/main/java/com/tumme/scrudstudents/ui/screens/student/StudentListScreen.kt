package com.tumme.scrudstudents.ui.screens.student

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.StudentListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    viewModel: StudentListViewModel = hiltViewModel(), // Hilt injects the ViewModel
    onNavigateToForm: () -> Unit = {},                 // Callback to navigate to Add/Edit form
    onNavigateToDetail: (Int) -> Unit = {}             // Callback to navigate to student details
) {
    val students by viewModel.students.collectAsState()
    // - collectAsState() converts StateFlow to Compose State
    // - Compose automatically recomposes when students list changes
    // - students is a snapshot of current student list
    //val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    // Main screen layout
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Students") }) // Screen title
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) { // Navigate to form on FAB click
                Text("+") // Simple "+" icon
            }
        }
    ) { padding -> // Scaffold content padding
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding) // Account for top bar & FAB
            .padding(16.dp)             // Inner padding
        ) {
            // Table header
            TableHeader(cells = listOf("DOB","Last", "First", "Gender", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f))

            Spacer(modifier = Modifier.height(8.dp))

            // Student list
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student ->
                    StudentRow(
                        student = student,
                        onEdit = { /* navigate to form prefilled (not implemented here) */ },
                        onDelete = { viewModel.deleteStudent(student) }, // Calls ViewModel -> Repository -> DAO -> DB
                        onView = { onNavigateToDetail(student.idStudent) }, // Navigate to details
                        onShare = { /* share intent */ }
                    )
                    // Compose recomposition:
                    // - When viewModel.deleteStudent() updates DB:
                    //   - getAllStudents() Flow emits new list
                    //   - _students StateFlow updates
                    //   - collectAsState triggers recomposition of LazyColumn
                }
            }
        }
    }
}

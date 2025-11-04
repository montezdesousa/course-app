package com.tumme.scrudstudents.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.ui.viewmodel.StudentListViewModel

/**
 * Screen to add a new Student.
 *
 * Purpose:
 * - Collect user input for a new student.
 * - Create a StudentEntity object and save it to the database via ViewModel.
 *
 * MVVM Data Flow:
 * UI input -> Compose State -> ViewModel.insertStudent() -> Repository -> DAO -> Room DB
 * Room DB updates trigger StateFlow emission -> Compose UI observing student list recomposes
 */
@Composable
fun StudentFormScreen(
    viewModel: StudentListViewModel = hiltViewModel(), // Hilt injects the ViewModel
    onSaved: ()->Unit = {} // Callback after saving (e.g., navigate back)
) {
    // Compose states for form inputs
    var id by remember { mutableIntStateOf((0..10000).random()) } // Random ID for new student
    var username by remember { mutableStateOf("") }                 // Username input
    var password by remember { mutableStateOf("") }                 // Password input
    var lastName by remember { mutableStateOf("") }                  // Last Name input
    var firstName by remember { mutableStateOf("") }                 // First Name input
    var dobText by remember { mutableStateOf("2000-01-01") } // yyyy-MM-dd
    var gender by remember { mutableStateOf(Gender.NotConcerned) }   // Gender selection

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Form fields
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(Modifier.height(8.dp))
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
        Spacer(Modifier.height(8.dp))

        // Gender selector simple
        Row {
            listOf(Gender.Male, Gender.Female, Gender.NotConcerned).forEach { g->
                Button(onClick = { gender = g }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(g.value)
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        // Save Button
        Button(onClick = {
            // Parse date, default to today if parsing fails
            val dob = dateFormat.parse(dobText) ?: Date()
            // Create StudentEntity
            val student = StudentEntity(
                idStudent = id,
                username = username,
                password = password,
                lastName = lastName,
                firstName = firstName,
                dateOfBirth = dob,
                gender = gender
            )
            // Insert into DB via ViewModel
            // - Calls viewModelScope.launch internally
            viewModel.insertStudent(student)
            // Callback after saving
            onSaved()
        }) {
            Text("Save")
        }
    }
}

package com.tumme.scrudstudents.ui.screens.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentCourseListScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogoutNavigate: () -> Unit = {}
) {
    var selectedLevel by remember { mutableStateOf(LevelCourse.P1) }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- Level of Study Dropdown ---
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedLevel.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Level") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                LevelCourse.entries.forEach { level ->
                    DropdownMenuItem(
                        text = { Text(level.value) },
                        onClick = {
                            selectedLevel = level
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        Button(onClick = {
            authViewModel.logout()
            onLogoutNavigate()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}

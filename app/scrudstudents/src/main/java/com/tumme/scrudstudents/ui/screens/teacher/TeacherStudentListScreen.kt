package com.tumme.scrudstudents.ui.screens.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherStudentListScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    userId: String?, // Logged-in student ID
    onLogoutNavigate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logout Button
        Button(onClick = {
            viewModel.logout()
            onLogoutNavigate()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}

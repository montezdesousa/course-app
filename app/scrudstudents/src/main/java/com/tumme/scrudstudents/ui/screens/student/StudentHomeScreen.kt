package com.tumme.scrudstudents.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateToCourses: () -> Unit = {},
    onNavigateToSubscriptions: () -> Unit = {},
    onNavigateToGrades: () -> Unit = {},
    onNavigateToFinalGradeSummary: () -> Unit = {},
    onLogoutNavigate: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome, ${authViewModel.currentUsername}!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Courses (filtered by student level)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToCourses() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Courses")
            }
        }

        // My Subscriptions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToSubscriptions() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("My Subscriptions")
            }
        }

        // My Grades
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToGrades() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("My Grades")
            }
        }

        // Final Grade Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onNavigateToFinalGradeSummary() },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Text("Final Grade Summary")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = {
                authViewModel.logout()
                onLogoutNavigate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
    }
}

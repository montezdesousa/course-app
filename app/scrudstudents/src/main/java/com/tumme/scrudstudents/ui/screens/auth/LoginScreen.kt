package com.tumme.scrudstudents.ui.screens.auth

import com.tumme.scrudstudents.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import com.tumme.scrudstudents.ui.viewmodel.LoginState
import com.tumme.scrudstudents.data.local.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onStudentLogin: (Int) -> Unit,
    onTeacherLogin: (Int) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Title ---
        Text(
            text = stringResource(R.string.login_message),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        // --- Username field ---
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.login_username_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // --- Password field ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.login_password_label)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        // --- Login Button ---
        Button(
            onClick = {
                authViewModel.login(username, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_button))
        }

        // --- UI reacts to login state ---
        when (val state = authViewModel.loginState) {
            is LoginState.Loading -> {
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }

            is LoginState.Error -> {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(state.message),
                    color = MaterialTheme.colorScheme.error
                )
            }

            is LoginState.Success -> {
                val auth = state.authResult
                LaunchedEffect(auth) {
                    when (auth.role) {
                        UserRole.STUDENT -> onStudentLogin(auth.userId)
                        UserRole.TEACHER -> onTeacherLogin(auth.userId)
                        UserRole.ADMIN -> TODO()
                        UserRole.NONE -> TODO()
                    }
                }
            }

            else -> {}
        }

        Spacer(Modifier.height(16.dp))

        // --- Register navigation ---
        TextButton(onClick = onNavigateToRegister) {
            Text(stringResource(R.string.login_register_prompt))
        }
    }
}

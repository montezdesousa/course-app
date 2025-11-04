package com.tumme.scrudstudents.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.viewmodel.AuthViewModel
import java.util.*

enum class UserRole { STUDENT, TEACHER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit = {}
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var gender by remember { mutableStateOf(Gender.Male) }
    var levelOfStudy by remember { mutableStateOf(LevelCourse.P1) }

    // Dropdown expansion states
    var genderExpanded by remember { mutableStateOf(false) }
    var levelExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // --- Role selector ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RoleRadioButton(selected = selectedRole == UserRole.STUDENT, text = "Student") {
                selectedRole = UserRole.STUDENT
            }
            Spacer(modifier = Modifier.width(16.dp))
            RoleRadioButton(selected = selectedRole == UserRole.TEACHER, text = "Teacher") {
                selectedRole = UserRole.TEACHER
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- First Name ---
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Last Name ---
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Username ---
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Password ---
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Confirm Password ---
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Gender Dropdown ---
        ExposedDropdownMenuBox(
            expanded = genderExpanded,
            onExpandedChange = { genderExpanded = !genderExpanded }
        ) {
            OutlinedTextField(
                value = gender.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = genderExpanded,
                onDismissRequest = { genderExpanded = false }
            ) {
                Gender.entries.forEach { g ->
                    DropdownMenuItem(
                        text = { Text(g.name) },
                        onClick = {
                            gender = g
                            genderExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Level of Study Dropdown (only for students) ---
        if (selectedRole == UserRole.STUDENT) {
            ExposedDropdownMenuBox(
                expanded = levelExpanded,
                onExpandedChange = { levelExpanded = !levelExpanded }
            ) {
                OutlinedTextField(
                    value = levelOfStudy.value,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Level of Study") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = levelExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = levelExpanded,
                    onDismissRequest = { levelExpanded = false }
                ) {
                    LevelCourse.entries.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level.value) },
                            onClick = {
                                levelOfStudy = level
                                levelExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- Register button ---
        Button(
            onClick = {
                if (
                    password == confirmPassword &&
                    username.isNotBlank() &&
                    firstName.isNotBlank() &&
                    lastName.isNotBlank()
                ) {
                    val dob = Date() // placeholder

                    if (selectedRole == UserRole.STUDENT) {
                        val student = StudentEntity(
                            username = username,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = dob,
                            gender = gender,
                            levelOfStudy = levelOfStudy,
                            photoUri = null
                        )
                        authViewModel.registerStudent(student)
                    } else {
                        val teacher = TeacherEntity(
                            username = username,
                            password = password,
                            firstName = firstName,
                            lastName = lastName,
                            dateOfBirth = dob,
                            gender = gender,
                            photoUri = null
                        )
                        authViewModel.registerTeacher(teacher)
                    }

                    onRegisterSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}

@Composable
fun RoleRadioButton(selected: Boolean, text: String, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp)) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text)
    }
}

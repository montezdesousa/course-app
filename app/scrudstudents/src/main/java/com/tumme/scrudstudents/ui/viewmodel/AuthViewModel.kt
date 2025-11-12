package com.tumme.scrudstudents.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.UserRole
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.repository.AuthRepository
import com.tumme.scrudstudents.data.repository.AuthRepository.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    var currentUserId: Int? by mutableStateOf(null)
        private set
    var currentUsername: String? by mutableStateOf(null)
        private set
    var currentUserRole: UserRole? by mutableStateOf(null)
        private set
    var currentUserLevelOfStudy: LevelCourse? by mutableStateOf(null)
        private set


    fun registerStudent(student: StudentEntity) {
        viewModelScope.launch {
            repo.registerStudent(student)
        }
    }

    fun registerTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repo.registerTeacher(teacher)
        }
    }

    var loginState by mutableStateOf<LoginState>(LoginState.Idle)
        private set

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginState = LoginState.Loading
            val result = repo.login(username, password)
            if (result != null) {
                // Save current user info for screens
                currentUserId = result.userId
                currentUsername = result.username
                currentUserRole = result.role
                currentUserLevelOfStudy = result.levelOfStudy
                loginState = LoginState.Success(result)
            } else {
                loginState = LoginState.Error("Invalid credentials")
            }
        }
    }

    fun logout() {
        currentUserId = null
        currentUserRole = null
        loginState = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val authResult: AuthResult) : LoginState()
    data class Error(val message: String) : LoginState()
}

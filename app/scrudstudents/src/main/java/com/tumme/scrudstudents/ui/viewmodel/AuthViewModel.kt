package com.tumme.scrudstudents.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.AuthRepository.AuthResult
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

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
            loginState = if (result != null) {
                LoginState.Success(result)
            } else {
                LoginState.Error("Invalid credentials")
            }
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val authResult: AuthResult) : LoginState()
    data class Error(val message: String) : LoginState()
}

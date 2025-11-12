package com.tumme.scrudstudents.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepo: StudentRepository
) : ViewModel() {

    val students: StateFlow<List<StudentEntity>> =
        studentRepo.getAllStudents()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    suspend fun getStudentById(studentId: Int): StudentEntity? {
        return studentRepo.getStudentById(studentId)
    }

    fun getStudentByIdFlow(studentId: Int): Flow<StudentEntity?> = flow {
        emit(studentRepo.getStudentById(studentId))
    }

    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        studentRepo.insertStudent(student)
        _events.emit("Student saved")
    }

    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        studentRepo.deleteStudent(student)
        _events.emit("Student deleted")
    }
}

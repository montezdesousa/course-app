package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the Student list screen.
 *
 * Purpose:
 * - Provides data and handles events for the UI.
 * - Mediates between Repository (data layer) and Compose UI.
 *
 * MVVM Role:
 * UI <-> ViewModel <-> Repository <-> DAO <-> Database
 *
 * Hilt:
 * - @HiltViewModel allows Hilt to inject dependencies (SCRUDRepository here).
 */
@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val repo: SCRUDRepository // Repository that handles Student CRUD operations
) : ViewModel() {

    /**
     * _students: internal StateFlow holding the current list of students.
     * - Uses repo.getAllStudents() Flow from DAO.
     * - stateIn converts a cold Flow into a hot StateFlow that Compose can observe.
     * - viewModelScope ties the Flow to the lifecycle of the ViewModel.
     * - SharingStarted.Lazily: starts collecting the Flow only when someone observes it.
     * - emptyList(): initial value before DB emits any data.
     *
     * Compose connection:
     * - In the UI, this StateFlow is collected using collectAsState():
     *      val students by viewModel.students.collectAsState()
     * - Any change in the student list (insert/delete) triggers recomposition
     *   automatically, updating the LazyColumn without manual refresh.
     */
    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Public read-only StateFlow for Compose UI to observe
    val students: StateFlow<List<StudentEntity>> = _students

    // UI event / error Flow
    /**
     * _events: internal MutableSharedFlow used to send one-time events like messages or errors.
     * - SharedFlow is suitable for events that should be handled only once.
     * events: read-only version exposed to Compose UI.
     */
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    /**
     * Delete a student.
     * - Launches a coroutine in viewModelScope.
     * - Calls repository to delete the student.
     * - Emits a one-time event ("Student deleted") for UI feedback.
     *
     * Compose integration:
     * - Deletion triggers repo.getAllStudents() Flow → _students updates → UI recomposes.
     */
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        repo.deleteStudent(student)
        _events.emit("Student deleted")
    }

    /**
     * Insert a student.
     * - Launches a coroutine in viewModelScope.
     * - Calls repository to insert the student.
     * - Emits a one-time event ("Student inserted") for UI feedback.
     *
     * Compose integration:
     * - Insertion triggers repo.getAllStudents() Flow → _students updates → UI recomposes.
     */
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        repo.insertStudent(student)
        _events.emit("Student inserted")
    }

    /**
     * Find a student by ID.
     * - Suspend function; must be called from a coroutine.
     * - Returns StudentEntity? (nullable) from the repository.
     * - Useful for "Edit Student" or detail screens.
     */
    suspend fun findStudent(id: Int) = repo.getStudentById(id)

}
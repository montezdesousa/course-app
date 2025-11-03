package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
) {
    // Students
    /**
     * Fetch all students as a Flow of a list.
     *
     * - Returns Flow<List<StudentEntity>> from the StudentDao.
     * - Flow allows observing changes in the database reactively.
     * - When a new student is inserted or deleted, the Flow emits a new list automatically.
     *
     * Data Flow:
     * StudentEntity (DB) -> StudentDao -> Repository -> ViewModel -> Compose UI
     *
     * Compose Implication:
     * - In the ViewModel, we can convert this Flow to StateFlow or LiveData.
     * - Compose will recompose any UI observing the state when the list changes.
     */
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    /**
     * Insert a student into the database.
     *
     * - Calls the suspend function of StudentDao to add or replace a student.
     * - Should be called from a coroutine, typically using viewModelScope in the ViewModel.
     *
     * Data Flow:
     * UI Event (e.g., "Add Student") -> ViewModel -> Repository -> StudentDao -> DB
     * DB change triggers getAllStudents() Flow to emit a new list -> UI recomposes
     */
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)
    /**
     * Delete a student from the database.
     *
     * - Calls the suspend function of StudentDao to remove the student.
     * - Also needs to be called from a coroutine in the ViewModel.
     *
     * Data Flow:
     * UI Event (e.g., "Delete Student") -> ViewModel -> Repository -> StudentDao -> DB
     * DB change triggers getAllStudents() Flow -> UI recomposes
     */
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)
    /**
     * Get a single student by ID.
     *
     * - Returns a StudentEntity? (nullable) if the student exists.
     * - Suspend function; call from coroutine.
     * - Useful for "Edit Student" or "View Student Details" screens.
     *
     * Data Flow:
     * UI Event (e.g., select student) -> ViewModel -> Repository -> StudentDao -> DB
     * Result returned to ViewModel -> State exposed to Compose -> UI recomposes
     */
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)
}
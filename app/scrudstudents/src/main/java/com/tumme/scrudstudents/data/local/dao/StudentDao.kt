package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the StudentEntity.
 *
 * Purpose:
 * - Provides abstracted methods to access and manipulate student data in the Room database.
 * - Defines the SQL queries used by Room.
 *
 * Role in MVVM data flow:
 * 1. DAO interacts directly with the database.
 * 2. Repository calls DAO functions and exposes data to ViewModel.
 * 3. ViewModel observes data (e.g., via Flow) and updates Compose UI states.
 */
@Dao
interface StudentDao {

    /**
     * Fetch all students from the database, ordered by lastName then firstName.
     *
     * Returns a Flow<List<StudentEntity>>:
     * - Flow allows reactive, asynchronous streaming of data.
     * - When the database changes (insert/update/delete), the Flow emits new lists automatically.
     *
     * Compose Implication:
     * - The ViewModel can collect this Flow and expose it as a State<List<StudentEntity>>.
     * - When the list changes, Compose recomposes UI components displaying the students.
     */
    @Query("SELECT * FROM students ORDER BY lastName, firstName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    /**
     * Insert a new student into the database.
     *
     * @param student The StudentEntity to insert.
     * onConflict = REPLACE means:
     * - If a student with the same idStudent exists, it will be replaced.
     *
     * Suspend function:
     * - Can be called from a coroutine or ViewModelScope.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    /**
     * Delete a student from the database.
     *
     * @param student The StudentEntity to remove.
     *
     * Suspend function:
     * - Safe to call in coroutine; avoids blocking the main thread.
     */
    @Delete
    suspend fun delete(student: StudentEntity)

    /**
     * Fetch a single student by their ID.
     *
     * @param id The unique idStudent of the student.
     * @return The StudentEntity if found, or null otherwise.
     *
     * Suspend function:
     * - Called from coroutine context.
     * - Useful for editing or displaying student details.
     */
    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1")
    suspend fun getStudentById(id: Int): StudentEntity?
}
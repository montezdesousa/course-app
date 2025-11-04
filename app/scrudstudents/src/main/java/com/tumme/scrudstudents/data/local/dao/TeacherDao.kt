package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeacherDao {

    /**
     * Fetch all teachers from the database, ordered by name.
     * Returns a Flow for reactive UI updates.
     */
    @Query("SELECT * FROM teachers ORDER BY firstName")
    fun getAllTeachers(): Flow<List<TeacherEntity>>

    /**
     * Insert a new teacher into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teacher: TeacherEntity)

    /**
     * Delete a teacher from the database.
     */
    @Delete
    suspend fun delete(teacher: TeacherEntity)

    /**
     * Fetch a single teacher by their ID.
     */
    @Query("SELECT * FROM teachers WHERE idTeacher = :id LIMIT 1")
    suspend fun getTeacherById(id: Int): TeacherEntity?

    /**
     * Authentication Query: Finds a teacher by matching username and password.
     * Used by AuthRepository for dummy login.
     *
     * @param username The teacher's username.
     * @param password The teacher's password.
     * @return The TeacherEntity if credentials match, or null.
     */
    @Query("SELECT * FROM teachers WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getTeacherByUsernameAndPassword(username: String, password: String): TeacherEntity?
}
